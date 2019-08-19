import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.personal.lycol.plugin.gen.dialog.CodeDialog;

/**
 * Created by Administrator on 2019/8/16.
 */
public class MybatisGeneratorCodeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        CodeDialog dialog = new CodeDialog();
        dialog.pack();
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.setSize(800,400);
    }
}
