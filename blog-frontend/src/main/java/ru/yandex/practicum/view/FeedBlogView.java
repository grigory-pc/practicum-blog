package ru.yandex.practicum.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
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
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.utility.Data;
import ru.yandex.practicum.utility.HttpRequestService;

@PageTitle("Feed Posts")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.LIST_SOLID)
public class FeedBlogView extends Div implements AfterNavigationObserver {
  private GridListDataView<PostPreviewDto> dataView;
  private Div errorMessage = new Div();
  private FormLayout addPostForm;
  private TextField titleField;
  private Upload imageUpload;
  private TextArea postTextArea;
  MultiSelectComboBox<TagDto> tagSelect = new MultiSelectComboBox<>("Теги");
  private Dialog addPostDialog;
  private MemoryBuffer imageBuffer;
  private Button saveButton;

  Grid<PostPreviewDto> grid = new Grid<>();

  public FeedBlogView() {
    addClassName("feedblog-view");
    setSizeFull();
    grid.setHeight("100%");
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
    grid.addComponentColumn(this::createCard);

    initFilterButton();
    initAddPostButton();
    add(grid);

    dataView = grid.getListDataView();

    loadTagsLocal();
  }

  private VerticalLayout createCard(PostPreviewDto postPreviewDto) {
    VerticalLayout card = new VerticalLayout();

    card.addClassName("card");
    card.setSpacing(false);
    card.setPadding(false);

    String postText = postPreviewDto.postText();
    String truncatedText = Data.truncateText(postText, 3);

    Image image = getImage(postPreviewDto.image());

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

    HorizontalLayout content = new HorizontalLayout();
    content.add(image, new VerticalLayout(name, post, actions));

    card.add(content, tagsLayout);

    card.addClickListener(click -> UI.getCurrent().navigate("/post/" + postPreviewDto.id()));

    return card;
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    grid.setItems(Data.getPosts());
  }

  private void initFilterButton() {
    Button clearFilterButton = new Button("Очистить фильтр");
    clearFilterButton.addClickListener(click -> clearFilter());

    FlexLayout buttonLayout = new FlexLayout(clearFilterButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    buttonLayout.addClassName("filter-button-layout");

    add(buttonLayout);
  }

  private void initAddPostButton() {
    Button addPostButton = new Button("Добавить пост");
    addPostButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addPostButton.addClickListener(click -> addPostDialog.open());

    FlexLayout buttonLayout = new FlexLayout(addPostButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
    buttonLayout.addClassName("add-button-layout");

    add(buttonLayout);

    createAddPostDialog();
  }

  private void createAddPostDialog() {
    imageBuffer = new MemoryBuffer();

    addPostDialog = new Dialog();
    addPostDialog.addClassName("custom-dialog");
    addPostDialog.setCloseOnEsc(true);
    addPostDialog.setCloseOnOutsideClick(true);
    addPostDialog.setModal(true);

    addPostForm = new FormLayout();
    addPostForm.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1),
        new FormLayout.ResponsiveStep("400px", 2)
    );

    titleField = new TextField("Заголовок");
    titleField.setRequired(true);
    titleField.setWidth("100%");

    imageUpload = new Upload(imageBuffer);
    imageUpload.setAcceptedFileTypes("image/*");
    imageUpload.setMaxFileSize(10 * 1024 * 1024); // 10MB
    imageUpload.setAutoUpload(true);
    imageUpload.setWidth("100%");

    postTextArea = new TextArea("Текст поста");
    postTextArea.setRequired(true);
    postTextArea.setHeight("200px");
    postTextArea.setWidth("100%");

    MultiSelectComboBox<TagDto> tagsSelect = new MultiSelectComboBox<>("Теги");
    tagsSelect.setItems(loadTagsLocal());
    tagsSelect.setItemLabelGenerator(TagDto::tagName);
    tagsSelect.setWidth("100%");

    saveButton = new Button("Сохранить");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(this::savePost);

    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setAlignSelf(FlexComponent.Alignment.END);
    buttonLayout.add(saveButton);

    addPostForm.add(
        titleField,
        imageUpload,
        postTextArea,
        tagsSelect,
        new HorizontalLayout(saveButton)
    );

    addPostDialog.add(addPostForm);
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

      if (HttpRequestService.sendPostToServer(postDto)) {
        clearForm();
      } else {
        errorMessage.setText("Не удалось сохранить данные");
        addPostForm.add(errorMessage);
        clearForm();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void loadTags() {
    Set<TagDto> tagDtos = HttpRequestService.loadTagsFromBackend();
    if (tagDtos.isEmpty()) {
      Notification.show("Ошибка получения тегов", 5000, Notification.Position.BOTTOM_CENTER);
    } else {
      tagSelect.setItems(tagDtos);
      tagSelect.setItemLabelGenerator(TagDto::tagName);
    }

  }

  private void clearForm() {
    titleField.clear();
    imageBuffer = new MemoryBuffer();
    postTextArea.clear();
    tagSelect.setValue(Set.of());
    titleField.focus();
    errorMessage.remove();
  }

  private Set<TagDto> loadTagsLocal() {
    return Set.of(new TagDto(1L, "test"), new TagDto(2L, "2024"), new TagDto(3L, "practicum"),
                  new TagDto(4L, "2025"), new TagDto(5L, "practicum"));
  }
}