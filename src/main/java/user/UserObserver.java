package user;

public interface UserObserver {

    //// Méthode appelée automatiquement lorsque l'état de la liste des users change.
    public void update(String action, String pseudo);
}


