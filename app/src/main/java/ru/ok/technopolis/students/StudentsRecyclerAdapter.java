package ru.ok.technopolis.students;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static ru.ok.technopolis.students.LoaderLargeImage.decodeSampledBitmapFromResource;

public class StudentsRecyclerAdapter extends RecyclerView.Adapter<StudentsRecyclerAdapter.StudentViewHolder> {

    public interface OnActionInRecyclerViewListener {
        void onClearFields();
        void onActionStudent(String fName, String sName, boolean gender, int photo);
    }

    public static final int NO_ACTION_STUDENT = -1;

    private Context context;
    private OnActionInRecyclerViewListener onActionInRecyclerViewListener;

    private List<Student> studentList = new ArrayList<>();
    private int actionStudent = NO_ACTION_STUDENT;

    //TODO сделать разделители

    public StudentsRecyclerAdapter(Context context){
        this.context = context;
        this.onActionInRecyclerViewListener = (OnActionInRecyclerViewListener) context;
    }

    public void saveStudent(Student newStudent){
        if (actionStudent == NO_ACTION_STUDENT){
            studentList.add(newStudent);
            notifyItemInserted(studentList.size() - 1);
            actionStudent = studentList.size() - 1;
        } else {
            Student student = studentList.get(actionStudent);
            student.setFirstName(newStudent.getFirstName());
            student.setSecondName(newStudent.getSecondName());
            student.setMaleGender(newStudent.isMaleGender());
            student.setPhoto(newStudent.getPhoto());
            notifyItemChanged(actionStudent);
        }
    }

    public void deleteStudent(){
        if (actionStudent != NO_ACTION_STUDENT){
            studentList.remove(actionStudent);
            notifyItemRemoved(actionStudent);
            actionStudent = NO_ACTION_STUDENT;
            onActionInRecyclerViewListener.onClearFields();
        } else {
            onActionInRecyclerViewListener.onClearFields();
        }
    }

    public void setActionStudent(int actionStudent) {
        this.actionStudent = actionStudent;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder studentViewHolder, int position) {
        studentViewHolder.bind(studentList.get(position));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatar;
        private TextView name;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.recycler_item__image);
            name = itemView.findViewById(R.id.recycler_item__text);
            //TODO графтческое выделение записи нажатой
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionStudent = getAdapterPosition();

                    Student student = studentList.get(getAdapterPosition());
                    onActionInRecyclerViewListener.onActionStudent(
                            student.getFirstName(),
                            student.getSecondName(),
                            student.isMaleGender(),
                            student.getPhoto()
                    );
                }
            });
        }

        private void bind(Student student){

            //TODO размеры картинки определить
            avatar.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), student.getPhoto(), 100, 100));

            String stringBuilder = student.getFirstName() +
                    " " +
                    student.getSecondName();
            name.setText(stringBuilder);
        }
    }
}
