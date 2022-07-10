package harsh.simpleTodo;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletionException;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    private DatabaseReference mDatabase;
    public FirebaseAuth mAuth;

    private ArrayList<Object> TodoList;
    RecyclerViewAdapter(ArrayList<Object> TodoList){
        this.TodoList = TodoList;
    }
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylist,parent,false);

        return new MyViewHolder(view);

    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Todo Todo = (Todo) TodoList.get(position);

        holder.datehead.setText(properDate(Todo.date()));

        List<String> events = new ArrayList<String>();
        List<String> time = new ArrayList<String>();
        String[] arrOfStr = Todo.getTitle().split(",");
        for (String a : arrOfStr){
            String[] aer = a.split("=");
            events.add(aer[0]);
            time.add(aer[1]);

        };
        final TextView[] tv = new TextView[10];
        final RadioButton[] cb = new RadioButton[10];
//
        for (int i=0; i<time.size(); i++)
        {
            tv[i] = new TextView(holder.cardView.getContext());
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams
                    ((int) ViewGroup.LayoutParams.WRAP_CONTENT,(int) ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 50;
            params.topMargin  = i*50;
            tv[i].setText(properTime(time.get(i)));
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); //ALIGN_PARENT_RIGHT / LEFT etc.
            tv[i].setPadding(20, 50, 20, 50);
            tv[i].setLayoutParams(params);
            holder.cardView.addView(tv[i]);
        }
        for (int i=0; i<time.size(); i++)
        {
            cb[i] = new RadioButton(holder.cardView.getContext());
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int) ViewGroup.LayoutParams.WRAP_CONTENT,(int) ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 50;
            params.topMargin  = i*50;
       //     cb[i].setText(time.get(i));
         //   cb.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cb[i].setPadding(20, 50, 20, 50);
            cb[i].setText(events.get(i).substring(1));
            cb[i].setLayoutParams(params);
            int finalI = i;
            cb[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    Log.d(TAG, "this"+events.get(finalI).substring(1) +"this");
//                    HashMap hashmap = new HashMap();
//                    hashmap.put(events,null);
                    mDatabase.child("Registered Users").child(currentUser.getUid()).child(Todo.date()).child(events.get(finalI).substring(1)).removeValue();

                    Toast.makeText(holder.cardView.getContext(), events.get(finalI).substring(1)
                            +" deleted", Toast.LENGTH_SHORT).show();
                }
            });
            holder.cardView.addView(cb[i]);
        }


    }

    @Override
    public int getItemCount() {
        return TodoList.size();
    }
    public String properDate(String date){

        String year=date.substring(0, 4);
        String month=date.substring(4,6 );
        String day=date.substring(6,8);
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("MMM dd yyyy");
        Date date2 = null;
        try {
            date2 = format1.parse(day+"/"+month+"/"+year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format2.format(date2 != null ? date2 : null);
    }
    public String properTime(String time){
        String hh=time.substring(0,2);
        String mm=time.substring(2,4);
        String shift=time.substring(4,6);
        return hh+":"+mm+" "+ shift;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private RelativeLayout cardView;
        private final TextView datehead;
       private final RelativeLayout rl;


        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.insidecardview);
            datehead=itemView.findViewById(R.id.datehead);
            rl = (RelativeLayout) itemView.findViewById(R.id.mainLayout);




        }
    }
}

