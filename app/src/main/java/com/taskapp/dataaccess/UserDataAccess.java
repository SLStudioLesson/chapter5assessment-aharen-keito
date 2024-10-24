package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.taskapp.model.User;

public class UserDataAccess {
    private final String filePath;

    public UserDataAccess() {
        filePath = "C:\\Users\\user\\Documents\\アセスメント\\chapter5assessment-aharen-keito\\app\\src\\main\\resources\\users.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public UserDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * メールアドレスとパスワードを基にユーザーデータを探します。
     * @param email メールアドレス
     * @param password パスワード
     * @return 見つかったユーザー
     */
    public User findByEmailAndPassword(String email, String password) {
        User user = null; // return出来なくなるため記述
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] value = line.split(",");
                if(!(value[2].equals(email) && value[3].equals(password)))continue;
                int code = Integer.parseInt(value[0]);
                String name = value[1];
                String userEmail = value[2]; // 引数と被るから userEmail にしている
                String userPassword = value[3];
                user = new User(code, name, userEmail, userPassword);
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }
        return user;
    }

    /**
     * コードを基にユーザーデータを取得します。
     * @param code 取得するユーザーのコード
     * @return 見つかったユーザー
     */
    public User findByCode(int code) {
        User user = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] value = line.split(",");
                int userCode = Integer.parseInt(value[0]);
                if(code != userCode) continue;
                String name = value[1];
                String email = value[2];
                String password = value[3];

                user = new User(userCode, name, email, password);
                break;
            }
            if (user == null) {
                System.out.println("ユーザーが見つかりません: コード " + code);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }
}
