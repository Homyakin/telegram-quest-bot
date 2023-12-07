package ru.homyakin.quest.bot.quest.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.models.AnswerType;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class QuestDaoMemory implements QuestDao {
    private final QuesStageMapper questStageMapper = new QuesStageMapper();
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public QuestDaoMemory(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Quest> getQuest(String questName) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "select * from quests where name = :name",
                    Map.of("name", questName),
                    (rs, rowNum) -> new Quest(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getBoolean("available"),
                        rs.getString("start_stage_name"),
                        getStages(questName)
                    )
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<QuestShort> getAllQuest() {
        return jdbcTemplate.getJdbcTemplate().query(
            "select * from quests",
            (rs, rowNum) -> new QuestShort(
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("available")
            )
        );
    }

    @Override
    public Optional<QuestStage> getStage(String questName, String stageName) {
        try {
            return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                    "select * from quest_stages where quest_name = :quest_name and name = :name",
                    Map.of(
                        "quest_name", questName,
                        "name", stageName
                    ),
                    questStageMapper::mapRow
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<QuestStage> getStages(String questName) {
        try {
            return jdbcTemplate.query(
                "select * from quest_stages where quest_name = :quest_name",
                Map.of("quest_name", questName),
                questStageMapper::mapRow
            );
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    private List<StageAvailableAnswer> getAvailableAnswers(String questName, String stageName) {
        try {
            return jdbcTemplate.query(
                "select aa.* from available_answers aa where quest_name = :quest_name and stage_name = :stage_name",
                Map.of(
                    "stage_name", stageName,
                    "quest_name", questName
                ),
                (rs, rowNum) -> new StageAvailableAnswer(
                    AnswerType.valueOf(rs.getString("type")),
                    rs.getString("next_stage_name"),
                    rs.getString("check_value")
                )
            );
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Transactional
    @Override
    public void save(Quest quest) {
        quest.stages().stream().forEach(stage -> saveStage(quest.name(), stage));
        jdbcTemplate.update(
            """
                insert into quests(name, description, available, start_stage_name)
                values(:name, :description, :available, :start_stage_name)
                on conflict(name) do update set
                description = excluded.description,
                available = excluded.available,
                start_stage_name = excluded.start_stage_name
                """,
            Map.of(
                "name", quest.name(),
                "description", quest.description(),
                "available", quest.available(),
                "start_stage_name", quest.startStageName()
            )
        );
    }

    private void saveStage(String questName, QuestStage questStage) {
        var parameters = new MapSqlParameterSource()
            .addValue("quest_name", questName)
            .addValue("name", questStage.name())
            .addValue("description", questStage.text())
            .addValue("photo_path", questStage.photoPath().orElse(null));
        jdbcTemplate.update(
            """
                insert into quest_stages(quest_name, name, description, photo_path)
                values(:quest_name, :name, :description, :photo_path)
                on conflict(quest_name, name) do update set
                description = excluded.description,
                photo_path = excluded.photo_path
                """,
            parameters
        );
        questStage.availableAnswers().forEach(answer -> saveAnswer(answer, questName, questStage.name()));
    }

    private void saveAnswer(StageAvailableAnswer answer, String questName, String stageName) {
        var parameters = new MapSqlParameterSource()
            .addValue("type", answer.answerType().name())
            .addValue("check_value", answer.value())
            .addValue("next_stage_name", answer.nextStageName())
            .addValue("quest_name", questName)
            .addValue("stage_name", stageName);
        jdbcTemplate.update(
            """
                insert into available_answers(type, check_value, next_stage_name, quest_name, stage_name)
                values(:type, :check_value, :next_stage_name, :quest_name, :stage_name)
                on conflict(next_stage_name, quest_name, stage_name) do update set
                type = excluded.type,
                check_value = excluded.check_value
                """,
            parameters
        );
    }

    private class QuesStageMapper implements RowMapper<QuestStage> {

        @Override
        public QuestStage mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new QuestStage(
                rs.getString("name"),
                rs.getString("description"),
                getAvailableAnswers(rs.getString("quest_name"), rs.getString("name")),
                Optional.ofNullable(rs.getString("photo_path"))
            );
        }
    }
}
