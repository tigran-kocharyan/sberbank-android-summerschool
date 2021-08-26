package ru.androidschool.sqliteexample.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.androidschool.sqliteexample.domain.BooksInteractor;
import ru.androidschool.sqliteexample.domain.model.Book;

public class MainViewModel extends ViewModel {

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<Book>> mBooksData = new MutableLiveData<>();
    private final BooksInteractor mInteractor;

    public MainViewModel(BooksInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mExecutor.shutdownNow();
    }

    public LiveData<List<Book>> getBooksData() {
        if (!mBooksData.hasObservers()) {
            initBooksData();
        }
        return mBooksData;
    }

    private void initBooksData() {
        mExecutor.submit(() -> {
            List<Book> books = mInteractor.getBooks();
            mBooksData.postValue(books);
        });
    }
}
