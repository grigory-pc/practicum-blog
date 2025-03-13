package ru.yandex.practicum.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import ru.yandex.practicum.dto.PostPreviewDto;
import ru.yandex.practicum.dto.PostSaveDto;
import ru.yandex.practicum.dto.TagDto;
import ru.yandex.practicum.view.services.HttpRequestService;
import ru.yandex.practicum.view.utility.Data;

@PageTitle("feed-blog")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.LIST_SOLID)
@Component
public class FeedBlogView extends Div implements AfterNavigationObserver {
  @Autowired
  private HttpRequestService httpRequestService;
  private GridListDataView<PostPreviewDto> dataView;
  private FormLayout addPostForm;
  private TextField titleField;
  private Upload imageUploader;
  private MemoryBuffer imageBuffer;
  private TextArea textArea;
  MultiSelectComboBox<TagDto> tagSelect = new MultiSelectComboBox<>("Теги");
  private Button saveButton;

  Grid<PostPreviewDto> grid = new Grid<>();

  public FeedBlogView() {
    addClassName("feedblog-view");
    setSizeFull();
    grid.setHeight("100%");
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
    grid.addComponentColumn(this::createCard);

    initFilterButton();

    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.addClassName("button-layout");
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    buttonLayout.setPadding(false);
    buttonLayout.setMargin(false);

    Button addPostButton = new Button("Добавить пост");
    addPostButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addPostButton.addClickListener(click -> {
      if (addPostForm == null) {
        createAddPostForm();
      }
      addPostForm.setVisible(true);
    });

    buttonLayout.add(addPostButton);

    add(grid, buttonLayout);

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

  private void createAddPostForm() {
    titleField = new TextField("Название поста");
    titleField.setRequired(true);

    imageBuffer = new MemoryBuffer();

    imageUploader = new Upload(imageBuffer);
    imageUploader.setAcceptedFileTypes("image/*");
    imageUploader.setAutoUpload(true);

    tagSelect = new MultiSelectComboBox<>("Теги");
    tagSelect.setPlaceholder("Выберите теги");

    textArea = new TextArea("Текст поста");
    textArea.setRequired(true);
    textArea.setHeight("200px");

    saveButton = new Button("Сохранить", click -> {
      savePost();
      addPostForm.setVisible(false);
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    addPostForm.add(titleField, imageUploader, textArea, tagSelect, saveButton);
  }

  private void savePost() {
    try {
      InputStream imageInputStream = imageBuffer.getInputStream();
      byte[] imageBytes = imageInputStream != null ? imageInputStream.readAllBytes() : null;

      Set<TagDto> selectedTags = tagSelect.getValue();

      Set<Long> tagIds = selectedTags.stream()
                                     .map(TagDto::id)
                                     .collect(Collectors.toSet());

      PostSaveDto postDto = new PostSaveDto(titleField.getValue(), imageBytes, textArea.getValue(),
                                            tagIds);

      if (httpRequestService.sendPostToServer(postDto)) {
        clearForm();
      } else {
        Notification.show("Ошибка создания поста", 5000, Notification.Position.BOTTOM_CENTER);
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void loadTags() {
    Set<TagDto> tagDtos = httpRequestService.loadTagsFromBackend();
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
    textArea.clear();
    tagSelect.setValue(Set.of());
    titleField.focus();
  }
  private Set<TagDto> loadTagsLocal() {
    return Set.of(new TagDto(1L, "test"), new TagDto(2L, "2024"), new TagDto(3L, "practicum"),
                  new TagDto(4L, "2025"), new TagDto(5L, "practicum"));
  }
}