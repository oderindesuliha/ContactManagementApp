package com.contacts.utils;

    import org.mindrot.jbcrypt.BCrypt;

    public class Password {


        public static String hashPassword(String password) {
            return BCrypt.hashpw(password, BCrypt.gensalt(14));
        }


        public static boolean checkPassword(String password, String hashedPassword) {
            return BCrypt.checkpw(password, hashedPassword);
        }
}
