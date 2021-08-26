package ru.androidschool.sqliteexample.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.androidschool.sqliteexample.data.repository.BooksRepositoryImpl;
import ru.androidschool.sqliteexample.domain.BooksInteractor;

class BooksViewModelFactory implements ViewModelProvider.Factory {

    private final Context mContext;

    public BooksViewModelFactory(Context context) {
        mContext = context;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new MainViewModel(
                new BooksInteractor(
                        new BooksRepositoryImpl(mContext)));
    }
}
