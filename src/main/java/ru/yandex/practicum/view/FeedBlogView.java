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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.TagDto;

@PageTitle("feed-blog")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.LIST_SOLID)
public class FeedBlogView extends Div implements AfterNavigationObserver {
  private static final String IMAGE_MAN_PATH = "C:\\Users\\Data\\Desktop\\man.jpg";
  private static final String IMAGE_WOMAN_PATH = "C:\\Users\\Data\\Desktop\\woman.jpg";
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
    String truncatedText = truncateText(postText, 3);
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
      Span tagSpan = new Span("#" + tag.tag());
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
    byte[] imageManBytes = getImageBytes(IMAGE_MAN_PATH);
    byte[] imageWomanBytes = getImageBytes(IMAGE_WOMAN_PATH);

    // Set some data when this view is displayed.
    List<PostPreviewDto> posts = Arrays.asList(
        createPost(imageManBytes, "John Smith",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   1000, 300, Set.of(new TagDto(1L, "test"), new TagDto(2L, "2024"))),
        createPost(imageWomanBytes, "Abagail Libbie",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   50, 400, Set.of(new TagDto(1L, "test"), new TagDto(2L, "2025"),
                                   new TagDto(3L, "practicum"))),
        createPost(imageManBytes, "Alberto Raya",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   1020, 5, Set.of(new TagDto(3L, "practicum"))),
        createPost(imageWomanBytes, "Emmy Elsner",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   100, 100, Set.of(new TagDto(2L, "2024"))),
        createPost(imageManBytes, "Alf Huncoot",
                   "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                   1000, 138, Set.of(new TagDto(1L, "test")))
    );

    grid.setItems(posts);
  }

  private static PostPreviewDto createPost(byte[] image, String title, String postText,
                                           Integer likes, Integer comments, Set<TagDto> tags) {

    return new PostPreviewDto(null, title, image, postText, likes, comments, tags);
  }

  private static Image getImage(byte[] imageBytes) {
    StreamResource resource = new StreamResource("image.png",
                                                 () -> new ByteArrayInputStream(imageBytes));

    return new Image(resource, "image");
  }

  private static byte[] getImageBytes(String path) {
    File fileImage = new File(path);

    try {
      return Files.readAllBytes(fileImage.toPath());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String truncateText(String text, int maxLines) {
    String[] lines = text.split("\n");
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < Math.min(lines.length, maxLines); i++) {
      result.append(lines[i]).append("\n");
    }

    return result.toString().trim();
  }

  private void filterByTag(TagDto tag) {
    dataView.setFilter(post -> post.tags().contains(tag));
  }

  private void clearFilter() {
    dataView.setFilter(null);
  }
}
