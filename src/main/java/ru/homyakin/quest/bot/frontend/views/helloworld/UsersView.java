package ru.homyakin.quest.bot.frontend.views.helloworld;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import ru.homyakin.quest.bot.frontend.views.MainLayout;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.dao.UserDao;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.UserAnswerResult;

import java.util.Comparator;

@PageTitle("Пользователи")
@Route(value = "users", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class UsersView extends VerticalLayout {

    private final UserDao userDao;
    private final QuestDao questDao;

    private final Select<QuestShort> selectQuestName = new Select<>();

    private final Select<Long> selectUserId = new Select<>();

    private final Select<String> selectStage = new Select<>();

    private final Button clearButton = new Button("Очистить");

    private final Grid<UserAnswerResult> answerGrid = new Grid<>();

    public UsersView(UserDao userDao, QuestDao questDao) {
        this.userDao = userDao;
        this.questDao = questDao;

        selectQuestName.setItems(questDao.getAllQuest());
        selectQuestName.setItemLabelGenerator(QuestShort::name);
        selectQuestName.addValueChangeListener(e -> refreshDataByQuestName(e.getValue()));

        selectUserId.setItems(userDao.getAnswers().stream().map(UserAnswerResult::userId).distinct().toList());
        selectUserId.addValueChangeListener(e -> refreshDataByUser(e.getValue()));

        selectStage.setItems(userDao.getAnswers().stream().map(UserAnswerResult::stageName).distinct().toList());
        selectStage.addValueChangeListener(e -> refreshDataByStage(e.getValue()));


        answerGrid.addColumn(UserAnswerResult::userId)
                .setHeader("User id");
        answerGrid.addColumn(UserAnswerResult::questName).setHeader("Quest name");
        answerGrid.addColumn(UserAnswerResult::stageName).setHeader("Stage name");
        answerGrid.addColumn(UserAnswerResult::answer).setHeader("Answer");
        answerGrid.addColumn(UserAnswerResult::answerTime).setHeader("Answer time");
        refreshData(null, null, null);

        clearButton.addClickListener(e -> clearSelects());
        add(
            new HorizontalLayout(selectUserId, selectQuestName, selectStage, clearButton),
            answerGrid
        );
    }

    private void clearSelects() {
        selectQuestName.clear();
        selectUserId.clear();
        selectStage.clear();
        refreshData(null, null, null);
    }

    private void refreshDataByStage(String stageName) {
        refreshData(selectQuestName.getValue(), selectUserId.getValue(), stageName);
    }

    private void refreshDataByQuestName(QuestShort quest) {
        refreshData(quest, selectUserId.getValue(), selectStage.getValue());
    }

    private void refreshDataByUser(Long userId) {
        refreshData(selectQuestName.getValue(), userId, selectStage.getValue());
    }

    private void refreshData(QuestShort quest, Long userId, String stageName) {
        final var answers = userDao.getAnswers()
                .stream()
                .filter(it ->
                        (quest == null || quest.name().equals(it.questName())) &&
                                (userId == null || userId.equals(it.userId())) &&
                                (stageName == null || stageName.equals(it.stageName()))
                )
                .sorted(Comparator.comparing(UserAnswerResult::questName).thenComparing(UserAnswerResult::answerTime))
                .toList();
        answerGrid.setItems(answers);
    }

}
