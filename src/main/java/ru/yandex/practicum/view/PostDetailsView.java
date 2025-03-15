package ru.yandex.practicum.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.stream.Collectors;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.view.utility.HttpRequestService;

@PageTitle("Post Details")
@Route("/post/:postId")
public class PostDetailsView extends Div implements AfterNavigationObserver {
  private Long postId;
  private FormLayout form;
  private TextField titleField;
  private Image image;
  private TextArea postTextArea;
  private HorizontalLayout tagsLayout;
  private VerticalLayout commentsLayout;
  private Button deleteButton;
  private Notification deleteFailedNotification;

  public PostDetailsView() {
    addClassName("post-details-view");
    setSizeFull();

    form = new FormLayout();
    form.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1),
        new FormLayout.ResponsiveStep("400px", 2)
    );

    titleField = new TextField("Заголовок");
    titleField.setReadOnly(true);

    postTextArea = new TextArea("Текст поста");
    postTextArea.setReadOnly(true);
    postTextArea.setHeight("200px");
    postTextArea.setWidthFull();

    tagsLayout = new HorizontalLayout();
    tagsLayout.addClassName("tags");

    commentsLayout = new VerticalLayout();
    commentsLayout.addClassName("comments");

    initDeletePostButton();

    add(form);
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    postId = event.getRouteParameters().getLong("postId").orElse(null);

    loadPostData();
  }

  private void loadPostData() {
    PostFullDto postFullDto = HttpRequestService.getPostFullDto(postId);

    if (postFullDto != null) {
      titleField.setValue(postFullDto.title());
      postTextArea.setValue(postFullDto.postText());

      if (postFullDto.image() != null) {
        StreamResource resource = new StreamResource("image.png",
                                                     () -> new ByteArrayInputStream(
                                                         postFullDto.image()));

        image = new Image(resource, "image");
        image.addClassName("post-image");
      }

      tagsLayout.removeAll();
      for (TagDto tag : postFullDto.tags()) {
        Span tagSpan = new Span("#" + tag.tagName());
        tagSpan.addClassName("tag");
        tagsLayout.add(tagSpan);
      }

      commentsLayout.removeAll();
      for (CommentDto comment : postFullDto.comments()) {
        commentsLayout.add(createCommentComponent(comment));
      }

      form.add(image, titleField, postTextArea, tagsLayout, commentsLayout);

    } else {
      Notification.show("Пост не найден", 3000, Notification.Position.BOTTOM_CENTER);
    }
  }

  private Component createCommentComponent(CommentDto comment) {
    Div commentContainer = new Div();
    commentContainer.addClassName("comment");

    VerticalLayout commentContent = new VerticalLayout();
    commentContent.setSpacing(false);
    commentContent.setPadding(false);

    Paragraph commentText = new Paragraph(comment.commentText());
    commentText.addClassName("comment-text");

    commentContainer.add(commentContent);
    commentContent.add(commentText);

    return commentContainer;
  }

  private void initDeletePostButton() {
    Button deleteButton = new Button("Удалить пост");
    deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    deleteButton.addClickListener(this::deletePost);

    FlexLayout buttonLayout = new FlexLayout(deleteButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    buttonLayout.addClassName("add-button-layout");

    add(buttonLayout);
  }

  private void deletePost(ClickEvent<Button> event) {
    System.out.println("Пост удален");
  }
}