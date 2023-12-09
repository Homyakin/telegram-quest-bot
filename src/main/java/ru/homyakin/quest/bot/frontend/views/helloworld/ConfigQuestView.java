package ru.homyakin.quest.bot.frontend.views.helloworld;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import ru.homyakin.quest.bot.frontend.views.MainLayout;
import ru.homyakin.quest.bot.quest.dao.QuestDao;
import ru.homyakin.quest.bot.quest.dao.UserDao;
import ru.homyakin.quest.bot.quest.models.Quest;
import ru.homyakin.quest.bot.quest.models.QuestShort;
import ru.homyakin.quest.bot.quest.models.UserAnswerResult;

import java.util.Comparator;

@PageTitle("Настройка квестов")
@Route(value = "quest", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ConfigQuestView extends VerticalLayout {

    private final QuestDao questDao;

    private final Button saveButton = new Button("Сохранить");

    private final TextField questName =  new TextField("Имя Квеста");

    private final TextField questDescription = new TextField("Описание квеста");

    private final Checkbox questAvailable = new Checkbox("Доступность квеста");


    public ConfigQuestView(QuestDao questDao) {
        this.questDao = questDao;

//        var quest = new Quest(
//
//        );

//        saveButton.addClickListener(e -> questDao.save(quest));

        add(
            saveButton,
            questName,
            questDescription,
            questAvailable
        );
    }

}
