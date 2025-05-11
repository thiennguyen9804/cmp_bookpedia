package com.plcoding.bookpedia.book.data.mapper

import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book

fun SearchedBookDto.toBook(): Book {
    return Book(
        id = id,
        title = title,
        imageUrl = if(coverKey != null)
                "https://covers.openlibrary.org/b/olid/${coverKey}-L.jpg"
            else
                "https://covers.openlibrary.org/b/olid/${coverAlternativeKe}-L.jpg",
        authors = authorName ?: emptyList(),
        description = null,
        languages = languages ?: emptyList(),
        firstPublishYear = firstPublishYear?.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions ?: 0
    )
}