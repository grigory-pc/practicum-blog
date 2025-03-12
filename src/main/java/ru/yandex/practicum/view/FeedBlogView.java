package ru.yandex.practicum.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import ru.yandex.practicum.dto.PostPreviewDto;

@PageTitle("feed-blog")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.LIST_SOLID)
public class FeedBlogView extends Div implements AfterNavigationObserver {
  private static final String IMAGE_MAN_PATH = "C:\\Users\\Data\\Desktop\\man.jpg";
  private static final String IMAGE_WOMAN_PATH = "C:\\Users\\Data\\Desktop\\woman.jpg";
  Grid<PostPreviewDto> grid = new Grid<>();

  public FeedBlogView() {
    addClassName("feedblog-view");
    setSizeFull();
    grid.setHeight("100%");
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
    grid.addComponentColumn(this::createCard);
    add(grid);
  }

  private HorizontalLayout createCard(PostPreviewDto postPreviewDto) {
    HorizontalLayout card = new HorizontalLayout();
    card.addClassName("card");
    card.setSpacing(false);
    card.getThemeList().add("spacing-s");

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

    Span post = new Span(postPreviewDto.postText());
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

    description.add(header, post, actions);
    card.add(image, description);
    return card;
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    byte[] imageManBytes = getImageBytes(IMAGE_MAN_PATH);
    byte[] imageWomanBytes = getImageBytes(IMAGE_WOMAN_PATH);

    // Set some data when this view is displayed.
    List<PostPreviewDto> posts = Arrays.asList(
        createPost(
            imageManBytes,
            "John Smith",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
            1000, 300),
        createPost(
            imageWomanBytes,
            "Abagail Libbie",
            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
            50, 400),
        createPost(
            imageManBytes,
            "Alberto Raya",

            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
            1020, 5),
        createPost(
            imageWomanBytes,
            "Emmy Elsner",

            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
            100, 100),
        createPost(
            imageManBytes,
            "Alf Huncoot",

            "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
            1000, 138)


    );

    grid.setItems(posts);
  }

  private static PostPreviewDto createPost(byte[] image, String title, String postText,
                                           Integer likes, Integer comments) {

    return new PostPreviewDto(null, title, image, postText, likes, comments, null);
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
}
