package ru.yandex.practicum.utils;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.exceptions.ValidationException;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@RequiredArgsConstructor
public class OffsetBasedPageRequest implements Pageable {
    private final int offset;
    private final int limit;
    private final Sort sort;

    public static Pageable of(Integer from, Integer size) {
        if (from == null && size == null) {
            from = 0;
            size = DEFAULT_PAGE_SIZE;
        }
        validateOrThrowException(from, size);
        return new OffsetBasedPageRequest(saveUnboxing(from), saveUnboxing(size), Sort.unsorted());
    }

    private static void validateOrThrowException(Integer from, Integer size) {
        if (saveUnboxing(size) < 1 || saveUnboxing(from) < 0) {
            throw new ValidationException("размер должен быть больше 0");
        }
    }

    public static int saveUnboxing(Integer value) {
        return Optional.ofNullable(value).orElse(0);
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(offset + limit, limit, sort);
    }

    @Override
    public Pageable previousOrFirst() {
        return new OffsetBasedPageRequest(offset, limit, sort);
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(offset, limit, sort);
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetBasedPageRequest(offset + limit * pageNumber, limit, sort);
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public boolean isPaged() {
        return Pageable.super.isPaged();
    }

    @Override
    public boolean isUnpaged() {
        return Pageable.super.isUnpaged();
    }

    @Override
    public Sort getSortOr(Sort sort) {
        return Pageable.super.getSortOr(sort);
    }

    @Override
    public Optional<Pageable> toOptional() {
        return Pageable.super.toOptional();
    }
}
