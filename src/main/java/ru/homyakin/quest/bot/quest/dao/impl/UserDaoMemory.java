package ru.homyakin.quest.bot.quest.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.homyakin.quest.bot.quest.dao.UserDao;
import ru.homyakin.quest.bot.quest.models.QuestStage;
import ru.homyakin.quest.bot.quest.models.StageAvailableAnswer;
import ru.homyakin.quest.bot.quest.models.UserAnswer;
import ru.homyakin.quest.bot.quest.models.UserAnswerResult;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoMemory implements UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserDaoMemory(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void setQuestStage(String questName, Long userId, QuestStage questStage) {
        jdbcTemplate.update(
                "insert into user_current_quest values(:user_id, :quest_name, :quest_stage) on conflict(user_id) do update set quest_name=excluded.quest_name, quest_stage=excluded.quest_stage",
                Map.of(
                        "user_id", userId,
                        "quest_name", questName,
                        "quest_stage", questStage.name()
                )
        );
    }

    @Override
    public void saveUserAnswer(
            String questName,
            QuestStage questStage,
            StageAvailableAnswer availableAnswer,
            Long userId,
            UserAnswer answer
    ) {
        jdbcTemplate.update(
                "insert into user2answers values(:user_id, :quest_name, :quest_stage, :user_answer)",
                Map.of(
                        "user_id", userId,
                        "quest_name", questName,
                        "quest_stage", questStage.name(),
                        "user_answer", answer.text()
                )
        );
    }

    @Override
    public Optional<String> getUserCurrentQuest(Long userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "select quest_name from user_current_quest where user_id = :user_id",
                    Map.of("user_id", userId),
                    String.class
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getUserCurrentStage(String questName, Long userId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    "select quest_stage from user_current_quest where user_id = :user_id and quest_name = :quest_name",
                    Map.of(
                            "user_id", userId,
                            "quest_name", questName
                    ),
                    String.class
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void clear(Long userId) {
        jdbcTemplate.update(
                "delete from user_current_quest where user_id = :user_id",
                Map.of("user_id", userId)
        );
    }

    @Override
    public List<UserAnswerResult> getAnswers() {
        return jdbcTemplate.getJdbcTemplate().query(
                "select * from user2answers",
                (rs, rowNum) -> new UserAnswerResult(
                        rs.getLong("user_id"),
                        rs.getString("quest_name"),
                        rs.getString("quest_stage"),
                        rs.getString("user_answer"),
                        rs.getTimestamp("date_insert").toLocalDateTime()
                )
        );
    }
}
