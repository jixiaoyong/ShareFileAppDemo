package cf.android666.myapplication.lanp2p;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cf.android666.myapplication.R;
import cf.android666.myapplication.timeline.view.TimeLineText;

/**
 * Created by jixiaoyong on 2018/7/26.
 * email:jixiaoyong1995@gmail.com
 */
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MViewHolder> {

    private Context mContext;
    private List<String> msgList;

    public RecyclerAdapter(Context mContext, List<String> msgList) {
        this.mContext = mContext;
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_msg, parent, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.timeLineText.setText(msgList.get(position));
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder{

        private TimeLineText timeLineText;

        public MViewHolder(View itemView) {
            super(itemView);
            timeLineText = itemView.findViewById(R.id.text);
        }
    }
}
