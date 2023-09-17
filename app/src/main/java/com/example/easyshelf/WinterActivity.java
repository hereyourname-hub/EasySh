package com.example.easyshelf;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WinterActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winter);

        // Инициализация базы данных Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Получение текущего пользователя
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Пользователь не аутентифицирован, выполните необходимые действия
            // В данном случае, можно вернуть пользователя на экран входа или выполнить другую обработку
            return;
        }

        // Настройка обработчика для кнопки "Добавить"
        Button btnAddShoe = findViewById(R.id.btn_add);
        btnAddShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddShoeDialog(); // Вызываем метод для отображения диалога
            }
        });

        // Загрузка данных об обуви пользователя
        loadShoeData();
    }

    private void showAddShoeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Инфлейтим макет для диалога
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_shoe, null);
        builder.setView(dialogView);
        final EditText etShoeNumber = dialogView.findViewById(R.id.et_shoe_number);
        final EditText etShoeName = dialogView.findViewById(R.id.et_shoe_name);

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Получаем номер отсека и название обуви из EditText
                String shoeNumber = etShoeNumber.getText().toString();
                String shoeName = etShoeName.getText().toString();

                // Создаем объект Shoe
                Shoe shoe = new Shoe(shoeNumber, shoeName);

                // Сохраняем данные в базе данных Firebase
                String userId = currentUser.getUid();
                DatabaseReference userShoesRef = databaseReference.child("users").child(userId).child("shoes");
                String key = userShoesRef.push().getKey();
                userShoesRef.child(key).setValue(shoe);
            }
        });

        builder.setNegativeButton("Отмена", null);

        builder.create().show();
    }

    private void loadShoeData() {
        // Загружаем данные об обуви пользователя из Firebase Realtime Database, используя UID текущего пользователя
        String userId = currentUser.getUid();
        DatabaseReference userShoesRef = databaseReference.child("users").child(userId).child("shoes");

        userShoesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout layoutShoes = findViewById(R.id.layout_shoes);
                layoutShoes.removeAllViews(); // Очищаем существующие данные перед загрузкой

                for (DataSnapshot shoeSnapshot : dataSnapshot.getChildren()) {
                    Shoe shoe = shoeSnapshot.getValue(Shoe.class);
                    if (shoe != null) {
                        String shoeInfo = "Отсек: " + shoe.getShoeNumber() + ", Название: " + shoe.getShoeName();
                        displayShoeInfo(shoeInfo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибки
            }
        });
    }

    private void displayShoeInfo(String shoeInfo) {
        // Создаем новый TextView для отображения информации о добавленной обуви
        TextView textView = new TextView(this);
        textView.setText(shoeInfo);

        // Устанавливаем размер текста
        textView.setTextSize(20);

        // Настраиваем параметры макета для текстового представления
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Применяем параметры макета к текстовому представлению
        textView.setLayoutParams(layoutParams);

        // Добавляем текстовое представление в LinearLayout для отображения
        LinearLayout layoutShoes = findViewById(R.id.layout_shoes);
        layoutShoes.addView(textView);

        // Добавляем сплошную линию (разделитель)
        View line = new View(this);
        layoutParams.setMargins(0, 0, 0, 25);
        line.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        ));
        line.setBackgroundColor(getResources().getColor(android.R.color.black));

        layoutShoes.addView(line);
    }
}
