package tk.atna.shortlyapp.presentation.model

import androidx.annotation.StringRes

class InputException(
    @StringRes val errorMessage: Int
) : IllegalArgumentException()