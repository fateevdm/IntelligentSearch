package intelligentsearch;

import org.junit.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.language.Russian;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

import static org.languagetool.JLanguageTool.ParagraphHandling;

/**
 * User: Dmitry Fateev
 * Date: 07.04.13
 * Time: 18:23
 */
public class SpellCheckerTest {

    @Test
    public void spellCheckerTest() throws IOException {
        String toCheck = "Наука, изучающя не только законы и закономерности общественного развития в целом, " +
                "но и конкретные процессы становления, развития и преобразования различных стран и народов во всем их многообразии " +
                "и неповторимости:";
        JLanguageTool tool = new JLanguageTool(new Russian());
        tool.activateDefaultPatternRules();
        List<RuleMatch> matches = tool.check(toCheck, true, ParagraphHandling.NORMAL);
        for (RuleMatch match : matches) {
            System.out.println("Potential error at line " +
                    match.getEndLine() + ", column " +
                    match.getColumn() + ": " + match.getMessage());
            System.out.println("Suggested correction: " +
                    match.getSuggestedReplacements());
            System.out.println("Rule is: " + match.getRule());
            System.out.println("Ошибка: " + match.getShortMessage());
        }
    }
}
