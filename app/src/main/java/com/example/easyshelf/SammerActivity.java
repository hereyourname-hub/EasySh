package com.example.easyshelf;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SammerActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private Map<String, Boolean> selectedShoes = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sammer);

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

        // Настройка обработчика для кнопки "Удалить"
        Button btnDelShoes = findViewById(R.id.btn_del);
        btnDelShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedShoes(); // Удаляем выбранные обуви
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

                // Создаем объект Shoe1
                Shoe1 shoe = new Shoe1(shoeNumber, shoeName);

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

    private void deleteSelectedShoes() {
        // Удаляем выбранные обуви
        Iterator<Map.Entry<String, Boolean>> iterator = selectedShoes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Boolean> entry = iterator.next();
            if (entry.getValue()) {
                // Если обувь выбрана для удаления, удаляем ее из базы данных Firebase
                String shoeKey = entry.getKey();
                DatabaseReference userShoesRef = databaseReference.child("users").child(currentUser.getUid()).child("shoes");
                userShoesRef.child(shoeKey).removeValue();
                // Удаляем запись из выбранных элементов
                iterator.remove();
            }
        }
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

                int shoeNumber = 1; // Начальное значение нумерации

                for (DataSnapshot shoeSnapshot : dataSnapshot.getChildren()) {
                    Shoe1 shoe = shoeSnapshot.getValue(Shoe1.class);
                    if (shoe != null) {
                        String shoeKey = shoeSnapshot.getKey();
                        String shoeInfo = "№ " + shoeNumber + " " + shoe.getShoeName();
                        displayShoeInfo(shoeKey, shoeInfo);

                        shoeNumber++; // Увеличиваем номер для следующей обуви
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибки
            }
        });
    }

    private void displayShoeInfo(String shoeKey, String shoeInfo) {
        // Создаем новый горизонтальный контейнер
        LinearLayout shoeContainer = new LinearLayout(this);
        shoeContainer.setOrientation(LinearLayout.HORIZONTAL);

        // Создаем новый TextView для отображения информации о добавленной обуви
        TextView textView = new TextView(this);
        textView.setText(shoeInfo);
        textView.setTextSize(20);

        // Добавляем TextView в горизонтальный контейнер
        shoeContainer.addView(textView);

        // Создаем CheckBox для выбора обуви
        CheckBox checkBox = new CheckBox(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Обновляем выбранное состояние обуви при изменении флажка
                selectedShoes.put(shoeKey, isChecked);
            }
        });

        // Добавляем CheckBox в горизонтальный контейнер
        shoeContainer.addView(checkBox);

        // Настраиваем параметры макета для горизонтального контейнера
        LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );
        shoeContainer.setLayoutParams(containerLayoutParams);

        // Устанавливаем обработчик клика для горизонтального контейнера
        shoeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Инвертируем состояние CheckBox при клике на контейнер
                checkBox.setChecked(!checkBox.isChecked());
            }
        });

        // Добавляем горизонтальный контейнер в LinearLayout для отображения
        LinearLayout layoutShoes = findViewById(R.id.layout_shoes);
        layoutShoes.addView(shoeContainer);

        // Добавляем сплошную линию разделителя
        View divider = new View(this);
        LinearLayout.LayoutParams dividerLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                1 // Высота линии в пикселях (можете настроить по вашему желанию)
        );
        divider.setLayoutParams(dividerLayoutParams);
        divider.setBackgroundColor(getResources().getColor(android.R.color.black));

        // Добавляем разделитель в LinearLayout
        layoutShoes.addView(divider);
    }
}
