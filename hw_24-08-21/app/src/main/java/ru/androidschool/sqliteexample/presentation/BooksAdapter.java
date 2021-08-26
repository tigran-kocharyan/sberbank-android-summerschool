package ru.androidschool.sqliteexample.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import ru.androidschool.sqliteexample.R;
import ru.androidschool.sqliteexample.domain.model.Book;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {

    private final List<Book> mBookList;

    public BooksAdapter(List<Book> bookList) {
        mBookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new BookViewHolder(inflater.inflate(R.layout.layout_book_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int position) {
        bookViewHolder.bind(mBookList.get(position));
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        private final TextView mAuthorView;
        private final TextView mTitleView;
        private final TextView mUuidView;
        private final TextView mShelfView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorView = itemView.findViewById(R.id.bookAuthor);
            mTitleView = itemView.findViewById(R.id.bookTitle);
            mUuidView = itemView.findViewById(R.id.bookUuid);
            mShelfView = itemView.findViewById(R.id.bookShelf);
        }

        void bind(@NonNull Book book) {
            mAuthorView.setText(String.format(Locale.getDefault(), "%d:%s", book.bookId, book.author));
            mTitleView.setText(book.title);
            mUuidView.setText(book.uuid);
            mShelfView.setText(String.valueOf(book.shelfNumber));
        }
    }
}
