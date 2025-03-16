package ru.yandex.practicum.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import ru.yandex.practicum.dto.CommentDto;
import ru.yandex.practicum.dto.PostFullDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.view.utility.RestService;

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
  private Dialog editPostDialog;
  private MemoryBuffer imageBuffer;
  private FormLayout editPostForm;
  private Upload imageUpload;
  private Button saveButton;
  MultiSelectComboBox<TagDto> tagSelect = new MultiSelectComboBox<>("Теги");
  private Div errorMessage = new Div();
  private TextField editTitleField;
  private TextArea editPostTextArea;

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

    add(form);
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    postId = event.getRouteParameters().getLong("postId").orElse(null);

    loadPostData();
    initDeletePostButton();
    initEditPostButton();
  }

  private void loadPostData() {
    Optional<PostFullDto> postFullDto = RestService.getPostFullDto(postId);

    if (postFullDto.isPresent()) {
      PostFullDto post = postFullDto.get();
      titleField.setValue(post.title());
      postTextArea.setValue(post.postText());

      if (post.image() != null) {
        StreamResource resource = new StreamResource("image.png",
                                                     () -> new ByteArrayInputStream(
                                                         post.image()));

        image = new Image(resource, "image");
        image.addClassName("post-image");
      }

      tagsLayout.removeAll();
      for (TagDto tag : post.tags()) {
        Span tagSpan = new Span("#" + tag.tagName());
        tagSpan.addClassName("tag");
        tagsLayout.add(tagSpan);
      }

      commentsLayout.removeAll();
      for (CommentDto comment : post.comments()) {
        commentsLayout.add(createCommentComponent(comment));
      }

      Icon likeIcon = new Icon(VaadinIcon.HEART_O);
      likeIcon.addClassName("like-icon");

      likeIcon.addClickListener(click -> {
        boolean isLiked = likePost(postId);
        if (isLiked) {
          likeIcon.getStyle().set("color", "red");
          likeIcon.setIcon(VaadinIcon.HEART);
        }
      });

      form.add(image, titleField, postTextArea, likeIcon, tagsLayout, commentsLayout);

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

    TextArea editCommentArea = new TextArea();
    editCommentArea.setValue(comment.commentText());
    editCommentArea.setVisible(false);

    Button editCommentButton = new Button("Редактировать");
    editCommentButton.addClassName("edit-button");
    editCommentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button saveCommentButton = new Button("Сохранить");
    saveCommentButton.addClassName("save-button");
    saveCommentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    saveCommentButton.setVisible(false);

    editCommentButton.addClickListener(click -> {
      commentText.setVisible(false);
      editCommentArea.setVisible(true);
      saveCommentButton.setVisible(true);
      editCommentButton.setVisible(false);
    });

    saveCommentButton.addClickListener(click -> {
      CommentDto updatedComment = new CommentDto(
          comment.id(),
          editCommentArea.getValue()
      );

      commentText.setText(editCommentArea.getValue());

      commentText.setVisible(true);
      editCommentArea.setVisible(false);
      saveCommentButton.setVisible(false);
      editCommentButton.setVisible(true);

      saveComment(updatedComment);
    });

    commentContainer.add(commentContent);
    commentContent.add(commentText, commentText, editCommentArea, editCommentButton,
                       saveCommentButton, initDeleteCommentButton());

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

  private FlexLayout initDeleteCommentButton() {
    Button deleteCommentButton = new Button("Удалить Комментарий");
    deleteCommentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    deleteCommentButton.addClickListener(this::deleteComment);

    FlexLayout buttonLayout = new FlexLayout(deleteCommentButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    buttonLayout.addClassName("add-button-layout");

    return buttonLayout;
  }

  private void initEditPostButton() {
    Button editPostButton = new Button("Редактировать пост");
    editPostButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    editPostButton.addClickListener(click -> editPostDialog.open());

    FlexLayout buttonLayout = new FlexLayout(editPostButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    buttonLayout.addClassName("add-button-layout");

    add(buttonLayout);

    createEditPostDialog();
  }

  private void deletePost(ClickEvent<Button> event) {
    if (RestService.deletePost(postId)) {
      UI.getCurrent().navigate(FeedBlogView.class);
    }
  }

  //ToDo доделать
  private void deleteComment(ClickEvent<Button> event) {
    if (RestService.deleteComment(1L)) {
      UI.getCurrent().navigate(PostDetailsView.class);
    }
  }

  private void saveComment(CommentDto commentDto) {
    if (RestService.sendCommentToServer(postId, commentDto)) {
      UI.getCurrent().navigate(PostDetailsView.class);
    }
  }

  private void createEditPostDialog() {
    imageBuffer = new MemoryBuffer();

    editPostDialog = new Dialog();
    editPostDialog.addClassName("custom-dialog");
    editPostDialog.setCloseOnEsc(true);
    editPostDialog.setCloseOnOutsideClick(true);
    editPostDialog.setModal(true);

    editPostForm = new FormLayout();
    editPostForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1),
        new FormLayout.ResponsiveStep("400px", 2)
    );

    editTitleField = new TextField("Заголовок");
    editTitleField.setValue(titleField.getValue());
    editTitleField.setWidth("100%");

    imageUpload = new Upload(imageBuffer);
    imageUpload.setAcceptedFileTypes("image/*");
    imageUpload.setMaxFileSize(10 * 1024 * 1024); // 10MB
    imageUpload.setAutoUpload(true);
    imageUpload.setWidth("100%");

    editPostTextArea = new TextArea("Текст поста");
    editPostTextArea.setValue(postTextArea.getValue());
    editPostTextArea.setHeight("200px");
    editPostTextArea.setWidth("100%");

    MultiSelectComboBox<TagDto> tagsSelect = new MultiSelectComboBox<>("Теги");
    tagsSelect.setItems(RestService.loadTagsFromBackend());
    tagsSelect.setItemLabelGenerator(TagDto::tagName);
    tagsSelect.setWidth("100%");

    saveButton = new Button("Сохранить");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(this::savePost);

    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setAlignSelf(FlexComponent.Alignment.END);
    buttonLayout.add(saveButton);

    editPostForm.add(
        editTitleField,
        imageUpload,
        editPostTextArea,
        tagsSelect,
        saveButton
    );

    editPostDialog.add(editPostForm);
  }

  private void savePost(ClickEvent<Button> event) {
    try {
      InputStream imageInputStream = imageBuffer.getInputStream();
      byte[] imageBytes = imageInputStream != null ? imageInputStream.readAllBytes() : null;

      PostSaveDto postDto = new PostSaveDto(
          titleField.getValue(),
          imageBytes,
          postTextArea.getValue(),
          tagSelect.getValue().stream()
                   .map(TagDto::id)
                   .collect(Collectors.toSet())
      );

      if (RestService.sendPostToServer(postDto)) {
        clearForm();
      } else {
        errorMessage.setText("Не удалось сохранить данные");
        editPostForm.add(errorMessage);
        clearForm();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void clearForm() {
    editTitleField.clear();
    imageBuffer = new MemoryBuffer();
    editPostTextArea.clear();
    tagSelect.setValue(Set.of());
    editTitleField.focus();
    errorMessage.remove();
  }

  private boolean likePost(Long postId) {
    return true;
  }
}