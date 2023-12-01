package com.csform.android.MB360.utilities;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.csform.android.MB360.insurance.enrollment.adapters.TopUpSumInsuredAdapter;
import com.csform.android.MB360.insurance.enrollment.repository.responseclass.topup.Si;

import java.util.List;

public class TopUpItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private RecyclerItemTouchHelperListener listener;
    List<Si> lstTopupData;

    public TopUpItemTouchHelper(int dragDirs, int swipeDirs,
                                RecyclerItemTouchHelperListener listerner,
                                List<Si> lstTopupData) {
        super(dragDirs, swipeDirs);
        this.listener = listerner;
        this.lstTopupData = lstTopupData;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            final View foregroundView = ((TopUpSumInsuredAdapter.TopUpsViewHolder) viewHolder).binding.llPremium;

            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((TopUpSumInsuredAdapter.TopUpsViewHolder) viewHolder).binding.llPremium;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                ItemTouchHelper.ACTION_STATE_DRAG, isCurrentlyActive);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        final View foregroundView = ((TopUpSumInsuredAdapter.TopUpsViewHolder) viewHolder).binding.llPremium;
        float newDX = dX;
        //viewHolder.itemView.setTranslationX(dX / 5);

      /*  if(newDX <= -350f) {
            newDX = -350f;
        }*/


        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, newDX, dY,
                ItemTouchHelper.ACTION_STATE_DRAG, isCurrentlyActive);

        drawBackground(viewHolder, dX, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final View foregroundView = ((TopUpSumInsuredAdapter.TopUpsViewHolder) viewHolder).binding.llPremium;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        Si topup = lstTopupData.get(position);
        if (topup.getSelected()==true) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        } else {
            return 0;
        }

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    private void drawBackground(RecyclerView.ViewHolder viewHolder, float dX, int actionState) {
        final View backgroundView = ((TopUpSumInsuredAdapter.TopUpsViewHolder) viewHolder).binding.deleteView;

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            backgroundView.setLeft((int) Math.max(dX, 0));
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (lstTopupData.get(viewHolder.getAdapterPosition()).getSelected()==true) {
            listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        }

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }


    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}
