package ru.yandex.practicum.view;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Project Blog for Yandex Practicum", shortName = "Practicum Blog")
@Theme("my-theme")
public class AppShell implements AppShellConfigurator {
}
