package com.plcoding.bookpedia.core.presentation

import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.error_disk_full
import cmp_bookpedia.composeapp.generated.resources.error_no_internet
import cmp_bookpedia.composeapp.generated.resources.error_request_timeout
import cmp_bookpedia.composeapp.generated.resources.error_serialization
import cmp_bookpedia.composeapp.generated.resources.error_unknown
import com.plcoding.bookpedia.core.domain.DataError

fun DataError.toUiText(): UiText {
    val stringResource = when(this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.UNKNOWN -> Res.string.error_unknown
        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.TOO_MANY_REQUEST -> Res.string.error_request_timeout
        DataError.Remote.NO_INTERNET -> Res.string.error_no_internet
        DataError.Remote.SERVER -> Res.string.error_unknown
        DataError.Remote.SERIALIZATION -> Res.string.error_serialization
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }

    return UiText.StringResourceId(stringResource)
}