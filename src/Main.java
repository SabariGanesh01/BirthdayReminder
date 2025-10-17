import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private final BirthdayService birthdayService;

    public Main(){
        this.birthdayService = new BirthdayService();
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.notifyEmployees();
    }

    private void notifyEmployees(){
        this.birthdayService.notifyEmployees();
    }


}