package ru.yandex.practicum.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.view.utility.Data;

@PageTitle("feed-blog")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.LIST_SOLID)
public class FeedBlogView extends Div implements AfterNavigationObserver {
  private GridListDataView<PostPreviewDto> dataView;

  Grid<PostPreviewDto> grid = new Grid<>();

  public FeedBlogView() {
    addClassName("feedblog-view");
    setSizeFull();
    grid.setHeight("100%");
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
    grid.addComponentColumn(this::createCard);
    add(grid);
    dataView = grid.getListDataView();
  }

  private VerticalLayout createCard(PostPreviewDto postPreviewDto) {
    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.addClassName("main-layout");
    mainLayout.setSpacing(false);
    mainLayout.setPadding(false);

    Button clearFilterButton = new Button("Очистить фильтр");
    clearFilterButton.addClickListener(click -> clearFilter());

    FlexLayout buttonLayout = new FlexLayout(clearFilterButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

    HorizontalLayout card = new HorizontalLayout();
    card.addClassName("card");
    card.setSpacing(false);
    card.getThemeList().add("spacing-s");

    String postText = postPreviewDto.postText();
    String truncatedText = Data.truncateText(postText, 3);
    Image image = getImage(postPreviewDto.image());

    VerticalLayout description = new VerticalLayout();
    description.addClassName("description");
    description.setSpacing(false);
    description.setPadding(false);

    HorizontalLayout header = new HorizontalLayout();
    header.addClassName("header");
    header.setSpacing(false);
    header.getThemeList().add("spacing-s");

    Span name = new Span(postPreviewDto.title());
    name.addClassName("name");

    Span post = new Span(truncatedText);
    post.addClassName("post");

    HorizontalLayout actions = new HorizontalLayout();
    actions.addClassName("actions");
    actions.setSpacing(false);
    actions.getThemeList().add("spacing-s");

    Icon likeIcon = VaadinIcon.HEART.create();
    likeIcon.addClassName("icon");
    Span likes = new Span(String.valueOf(postPreviewDto.countLikes()));
    likes.addClassName("likes");
    Icon commentIcon = VaadinIcon.COMMENT.create();
    commentIcon.addClassName("icon");
    Span comments = new Span(String.valueOf(postPreviewDto.countComments()));
    comments.addClassName("comments");

    actions.add(likeIcon, likes, commentIcon, comments);

    HorizontalLayout tagsLayout = new HorizontalLayout();
    tagsLayout.addClassName("tags");

    for (TagDto tag : postPreviewDto.tags()) {
      Span tagSpan = new Span("#" + tag.tagName());
      tagSpan.addClassName("tag");

      tagSpan.addClickListener(click -> filterByTag(tag));

      tagsLayout.add(tagSpan);
    }

    description.add(header, post, actions, tagsLayout);
    card.add(image, description);

    mainLayout.add(buttonLayout, card);

    return mainLayout;
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    grid.setItems(Data.getPosts());
  }

  private static Image getImage(byte[] imageBytes) {
    StreamResource resource = new StreamResource("image.png",
                                                 () -> new ByteArrayInputStream(imageBytes));

    return new Image(resource, "image");
  }

  private void filterByTag(TagDto tag) {
    dataView.setFilter(post -> post.tags().contains(tag));
  }

  private void clearFilter() {
    dataView.setFilter(null);
  }
}
