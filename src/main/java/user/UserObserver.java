package user;

public interface UserObserver {

    public void updateFromUser(String action, String pseudo, String oldPseudo);
}


