package ru.homyakin.quest.bot.quest.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
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
                                    getQuestStageById(rs.getInt("start_stage_id")).orElseThrow()
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
                            (rs, rowNum) -> new QuestStage(
                                    rs.getString("name"),
                                    rs.getString("description"),
                                    getAvailableAnswers(rs.getInt("id")),
                                    Optional.ofNullable(rs.getString("photo_path"))
                            )
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<StageAvailableAnswer> getAvailableAnswers(int stageId) {
        try {
            return jdbcTemplate.query(
                    "select aa.* from available_answers aa join stage2answers sa on aa.id = sa.answer_id where stage_id = :stage_id",
                    Map.of("stage_id", stageId),
                    (rs, rowNum) -> new StageAvailableAnswer(
                            rs.getString("name"),
                            AnswerType.valueOf(rs.getString("type")),
                            getQuestStageById(rs.getInt("next_stage_id")),
                            rs.getString("check_value")
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    private Optional<QuestStage> getQuestStageById(int id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "select * from quest_stages where id = :id",
                            Map.of("id", id),
                            (rs, rowNum) -> new QuestStage(
                                    rs.getString("name"),
                                    rs.getString("description"),
                                    getAvailableAnswers(rs.getInt("id")),
                                    Optional.ofNullable(rs.getString("photo_path"))
                            )
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void save(Quest quest) {
        int firstStageId = saveStage(quest.name(), quest.startStage());
        jdbcTemplate.update(
                "insert into quests(name, description, available, start_stage_id)" +
                        "values(:name, :description, :available, :start_stage_id)" +
                        "on conflict(name) do update set " +
                        "description = excluded.description," +
                        "available = excluded.available," +
                        "start_stage_id = excluded.start_stage_id",
                Map.of(
                        "name", quest.name(),
                        "description", quest.description(),
                        "available", quest.available(),
                        "start_stage_id", firstStageId
                )
        );
    }

    private int saveStage(String questName, QuestStage questStage) {
        var parameters = new MapSqlParameterSource()
                .addValue("quest_name", questName)
                .addValue("name", questStage.name())
                .addValue("description", questStage.text())
                .addValue("photo_path", questStage.photoPath().orElse(null));
//        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
//                .withTableName("quest_stages")
//                .usingGeneratedKeyColumns("id");
//        int stageId = insert.executeAndReturnKey(parameters).intValue();
        int stageId = jdbcTemplate.queryForObject(
                "insert into quest_stages(quest_name, name, description, photo_path)" +
                        "values(:quest_name, :name, :description, :photo_path)" +
                        "on conflict(quest_name, name) do update set " +
                        "description = excluded.description," +
                        "photo_path = excluded.photo_path " +
                        "returning id",
                parameters,
                Integer.class
        );
        jdbcTemplate.update(
                "delete from stage2answers where stage_id = :stage_id",
                Map.of("stage_id", stageId)
        );
        questStage.availableAnswers().forEach(answer -> saveAnswer(answer, stageId, questName));
        return stageId;
    }

    private void saveAnswer(StageAvailableAnswer answer, int stageId, String questName) {
        var nextStageId = saveStage(questName, answer.nextStage().orElseThrow());
        var parameters = new MapSqlParameterSource()
                .addValue("name", answer.name())
                .addValue("type", answer.answerType().name())
                .addValue("check_value", answer.value())
                .addValue("next_stage_id", nextStageId);
//        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
//                .withTableName("available_answers")
//                .usingGeneratedKeyColumns("id");
//        int answerId = insert.executeAndReturnKey(parameters).intValue();
        int answerId = jdbcTemplate.queryForObject(
                "insert into available_answers(name, type, check_value, next_stage_id)" +
                        "values(:name, :type, :check_value, :next_stage_id)" +
                        "on conflict(name) do update set " +
                        "type = excluded.type," +
                        "check_value = excluded.check_value, " +
                        "next_stage_id = excluded.next_stage_id " +
                        "returning id",
                parameters,
                Integer.class
        );
        parameters = new MapSqlParameterSource()
                .addValue("stage_id", stageId)
                .addValue( "answer_id", answerId);
        SimpleJdbcInsert insertLink = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("stage2answers");
        insertLink.execute(parameters);
    }
}
