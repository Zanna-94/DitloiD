package giava.menmath.ditloid.User;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import giava.menmath.ditloid.MyApplication;

/**
 * This class create the file in which it'll be written user state
 *
 * @author MenMath.GiaVa
 */
public class UserDao {

    private static String file_name = "UserInfo";

    public static void serializza(UserInfo info) throws IOException {

        FileOutputStream fos = MyApplication.getAppContext().openFileOutput(file_name,
                Context.MODE_PRIVATE);

        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(info);
        os.close();
        fos.close();

    }

    public static UserInfo deserializza() throws IOException {

        try {

            FileInputStream fis = MyApplication.getAppContext().openFileInput(file_name);
            ObjectInputStream is = new ObjectInputStream(fis);

            UserInfo userInfo = (UserInfo) is.readObject();

            is.close();
            fis.close();

            return userInfo;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return UserInfo.getInstance();
        }

    }

    public static String getFile_name() {
        return file_name;
    }

    public static void setFile_name(String file_name) {
        UserDao.file_name = file_name;
    }
}
