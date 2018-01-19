package bapspatil.wallcards.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.Objects;

import bapspatil.wallcards.R;
import bapspatil.wallcards.data.FavsContract;
import bapspatil.wallcards.models.ProfileImage;
import bapspatil.wallcards.models.URLs;
import bapspatil.wallcards.models.User;
import bapspatil.wallcards.models.Wallpaper;
import bapspatil.wallcards.utils.GlideApp;

/**
 * Created by bapspatil
 */

public class FavoritesWidgetService extends RemoteViewsService {
    private Bitmap imageBitmap;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Cursor cursor = this.getContentResolver().query(
                FavsContract.FavsEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.close();
        }
        return new WidgetDataProvider(this, intent, cursor);
    }

    public class WidgetDataProvider implements RemoteViewsFactory {
        private Context context;
        private Intent intent;
        private Cursor mCursor = null;

        public WidgetDataProvider(Context context, Intent intent, Cursor mCursor) {
            this.context = context;
            this.intent = intent;
            this.mCursor = mCursor;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if (mCursor != null) {
                mCursor.close();
            }
            final long identityToken = Binder.clearCallingIdentity();
            mCursor = FavoritesWidgetService.this.getContentResolver().query(FavsContract.FavsEntry.CONTENT_URI, null, null, null, null);
            Binder.restoreCallingIdentity(identityToken);
        }

        @Override
        public void onDestroy() {
            if (mCursor != null) {
                mCursor.close();
                mCursor = null;
            }
        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position == AdapterView.INVALID_POSITION ||
                    mCursor == null || !mCursor.moveToPosition(position)) {
                return null;
            }
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
            mCursor.moveToPosition(position);

            remoteViews.setTextViewText(R.id.list_wallcard_text, mCursor.getString(mCursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_USER_NAME)));

            int id = mCursor.getInt(mCursor.getColumnIndex(FavsContract.FavsEntry._ID));
            String null_state = "null";
            if (Objects.equals(id, null_state)) {
                Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_white);
                remoteViews.setImageViewBitmap(R.id.list_wallcard_icon, icon);
            } else {
                try {
                    Bitmap bitmap = GlideApp.with(context)
                            .asBitmap()
                            .centerCrop()
                            .load(mCursor.getString(mCursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_URL)))
                            .into(50, 50)
                            .get();
                    remoteViews.setImageViewBitmap(R.id.list_wallcard_icon, bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            final Intent fillInIntent = new Intent();
            Wallpaper wallpaper = new Wallpaper();
            wallpaper.setId(mCursor.getString(mCursor.getColumnIndex(FavsContract.FavsEntry._ID)));
            wallpaper.setColor(mCursor.getString(mCursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_COLOR)));
            wallpaper.setLikes(mCursor.getInt(mCursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_LIKES)));
            URLs urls = new URLs(mCursor.getString(mCursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_URL)));
            wallpaper.setUrls(urls);
            ProfileImage profileImage = new ProfileImage(mCursor.getString(mCursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_USER_PIC)));
            User user = new User(mCursor.getString(mCursor.getColumnIndex(FavsContract.FavsEntry.COLUMN_USER_NAME)),
                    profileImage);
            wallpaper.setUser(user);

            fillInIntent.putExtra("wallpaper", wallpaper);
            remoteViews.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

            return remoteViews;


        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
