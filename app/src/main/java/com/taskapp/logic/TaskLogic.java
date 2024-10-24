package com.taskapp.logic;

import java.time.LocalDate;
import java.util.List;

import com.taskapp.dataaccess.LogDataAccess;
import com.taskapp.dataaccess.TaskDataAccess;
import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.Log;
import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskLogic {
    private final TaskDataAccess taskDataAccess;
    private final LogDataAccess logDataAccess;
    private final UserDataAccess userDataAccess;

    public TaskLogic() {
        taskDataAccess = new TaskDataAccess();
        logDataAccess = new LogDataAccess();
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * 
     * @param taskDataAccess
     * @param logDataAccess
     * @param userDataAccess
     */
    public TaskLogic(TaskDataAccess taskDataAccess, LogDataAccess logDataAccess, UserDataAccess userDataAccess) {
        this.taskDataAccess = taskDataAccess;
        this.logDataAccess = logDataAccess;
        this.userDataAccess = userDataAccess;
    }

    /**
     * 全てのタスクを表示します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findAll()
     * @param loginUser ログインユーザー
     */
    public void showAll(User loginUser) {
        List<Task> tasks = taskDataAccess.findAll();
        tasks.forEach(task -> {
            // 0→未着手、1→着手中、2→完了
            /*
             * タスクを担当するユーザーの名前が表示できるようにすること
             * 担当者が今ログインしてるユーザーなら、「あなたが担当しています」と表示する
             * そうでないなら、担当してるユーザー名を表示する
             */

            // 1. タスク名：taskA, 担当者名：あなたが担当しています, ステータス：未着手
            System.out.print(task.getCode() + ". タスク名:" + task.getName() + ", 担当者名:");
            User user = task.getRepUser();
            if (user.getCode() == loginUser.getCode()) {
                String a = "あなたが担当しています";
                System.out.print(a);
            } else {
                System.out.print(user.getName() + "が担当しています");
                // User user = task.getRepUser();
                // if (user != null) {
                // System.out.print(user.getName() + "が担当しています");
                // } else {
                // System.out.println( "aaaaaaa");
                // }
            }
            System.out.print(", ステータス:");
            if (task.getStatus() == 0) {
                System.out.println("未着手");
            } else if (task.getStatus() == 1) {
                System.out.println("着手中");
            } else if (task.getStatus() == 2) {
                System.out.println("完了");
            }
        });
    }

    /**
     * 新しいタスクを保存します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#save(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code        タスクコード
     * @param name        タスク名
     * @param repUserCode 担当ユーザーコード
     * @param loginUser   ログインユーザー
     * @throws AppException ユーザーコードが存在しない場合にスローされます
     */
    /*
     * タスクコードを入力させるときは「タスクコードを入力してください：」と表示する
     * タスク名を入力させるときは「タスク名を入力してください：」と表示する
     * 担当するユーザーコードを入力させるときは「担当するユーザーのコードを選択してください：」と表示する
     */
    public void save(int code, String name, int repUserCode,
            User loginUser) throws AppException {
        User user = userDataAccess.findByCode(repUserCode);
        if (user == null) {
            throw new AppException("存在するユーザーコードを入力してください。");
        }
        if (user.getName() == null) {
            throw new AppException("存在するタスク名を入力してください。");
        }
        if (user.getCode() == 0) {
            throw new AppException("存在する担当するユーザーのコードを入力してください。");
        }
        Log log = new Log(code, loginUser.getCode(), 0,
                LocalDate.now());
        Task task = new Task(code, name, 0, user);
        logDataAccess.save(log);
        taskDataAccess.save(task);
        System.out.println(task.getName() + "の登録が完了しました。");
    }

    /**
     * タスクのステータスを変更します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#update(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code      タスクコード
     * @param status    新しいステータス
     * @param loginUser ログインユーザー
     * @throws AppException タスクコードが存在しない、またはステータスが前のステータスより1つ先でない場合にスローされます
     */

    public void changeStatus(int code, int status,
    User loginUser) throws AppException {
        Task task = taskDataAccess.findByCode(code);
        if(task == null){
            throw new AppException("存在するタスクコードを入力してください");
        }
        int status1 = task.getStatus();
        if (status != status1 + 1 ) {
            throw new AppException("ステータスは、前のステータスより1つ先のもののみを選択してください");
        }
        
        Task updateTask = new Task(task.getCode(),task.getName(),status,task.getRepUser());
        taskDataAccess.update(updateTask);
        Log log = new Log(code, loginUser.getCode(), status, LocalDate.now());
        logDataAccess.save(log);
        
        System.out.println("ステータスの変更が完了しました。" );
        }
        /*
         * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
         * 
     * @see com.taskapp.dataaccess.TaskDataAccess#update(com.taskapp.model.Task)
     * 
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.




    /**
     * タスクを削除します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#delete(int)
     * @see com.taskapp.dataaccess.LogDataAccess#deleteByTaskCode(int)
     * @param code タスクコード
     * @throws AppException タスクコードが存在しない、またはタスクのステータスが完了でない場合にスローされます
     */
    // public void delete(int code) throws AppException {
    // }
}