package io.toast.tk.test.runtime;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.toast.tk.dao.domain.impl.test.block.CommentBlock;
import io.toast.tk.dao.domain.impl.test.block.IBlock;
import io.toast.tk.dao.domain.impl.test.block.ITestPage;
import io.toast.tk.dao.domain.impl.test.block.TestBlock;
import io.toast.tk.dao.domain.impl.test.block.VariableBlock;
import io.toast.tk.dao.domain.impl.test.block.WebPageBlock;
import io.toast.tk.dao.domain.impl.test.block.line.WebPageConfigLine;
import io.toast.tk.runtime.parse.TestParser;

public class TestParserTestCase_3 {

    @Test
    public void testParserBlocks() throws IllegalArgumentException, IOException {
        TestParser par = new TestParser();
        String testString = ("$valeur:=variable\n" +
                "$valeur2:=\"\"\"\n" +
                "SELECT * T_TABLE\n" +
                "WHERE VALUE = \'42\'\n" +
                "\"\"\"\n" +
                "\n" +
                "Ceci est une ligne de commentaire.\n" +
                "Ceci est aussi une ligne de commentaire.\n" +
                "\n" +
                "|| scenario || swing ||\n" +
                "| @swing Saisir *$valeur* dans *ChooseApplicationRusDialog.applicationBox* |\n" +
                "| @service Cliquer sur *ChooseApplicationRusDialog.OK* |\n" +
                "| @service Cliquer sur *ChooseApplicationRusDialog.FIN* |\n" +
                "| @toto Cliquer sur *ChooseApplicationRusDialog.FIN* |\n" +
                "| Cliquer sur *ChooseApplicationRusDialog.KO* |\n" +
                "| @swing:connector Saisir *valeur* dans *ChooseApplicationRusDialog.applicationBox* |\n" +
                "\n" +
                "Encore des commentaires\n" +
                "$valeur2:=\"\"\"\n" +
                "SELECT * T_TABLE\n" +
                "WHERE VALUE = \'42\'\n" +
                "\"\"\"\n" +
                "\n" +
                "Toujours des commentaires\n" +
                "\n" +
                "|| scenario || service ||\n" +
                "| @swing Saisir *valeur* dans *ChooseApplicationRusDialog.applicationBox* |\n" +
                "| @service Cliquer sur *ChooseApplicationRusDialog.OK* |\n" +
                "| @service Cliquer sur *ChooseApplicationRusDialog.FIN* |\n" +
                "| @toto Cliquer sur *ChooseApplicationRusDialog.FIN* |\n" +
                "| Cliquer sur *ChooseApplicationRusDialog.KO* |\n" +
                "| @swing:connector Saisir *valeur* dans *ChooseApplicationRusDialog.applicationBox* |") + "\n";

        ITestPage testPage = par.readString(testString, null);
        Assert.assertNotNull(testPage);
        Assert.assertNotNull(testPage.getBlocks());
        int i = 0;
        IBlock block = testPage.getBlocks().get(i);
        Assert.assertEquals("variable", block.getBlockType());
        VariableBlock variableBlock = (VariableBlock) block;
        Assert.assertEquals(2, variableBlock.getBlockLines().size());
        i++;
        Assert.assertNotNull(testPage.getBlocks().get(i));
        Assert.assertEquals("comment", testPage.getBlocks().get(i).getBlockType());
        String commentLine = ((CommentBlock) testPage.getBlocks().get(i)).getLines().get(0);
        Assert.assertTrue(commentLine.startsWith("Ceci est une ligne de commentaire."));
        i++;
        Assert.assertEquals("test", testPage.getBlocks().get(i).getBlockType());
        i++;
        Assert.assertEquals("comment", testPage.getBlocks().get(i).getBlockType());
        i++;
        Assert.assertEquals("variable", testPage.getBlocks().get(i).getBlockType());
        i++;
        Assert.assertEquals("comment", testPage.getBlocks().get(i).getBlockType());
        commentLine = ((CommentBlock) testPage.getBlocks().get(i)).getLines().get(0);
        Assert.assertTrue(commentLine.startsWith("Toujours des commentaires"));
        i++;
        Assert.assertEquals("test", testPage.getBlocks().get(i).getBlockType());
        TestBlock testBlock = (TestBlock) testPage.getBlocks().get(i);
        Assert.assertEquals("Last scenario has 6 test lines", 6, testBlock.getBlockLines().size());
    }

    @Test
    public void testSetupBlocks() throws IllegalArgumentException, IOException {
        TestParser par = new TestParser();
        String testString = "h1. Description des pages\n" +
                "\n" +
                "|| setup || web page | GoogleSearchPage |\n" +
                "| name | type | locator | method | position |\n" +
                "| search | input | lst-ib | ID | 0 |\n" +
                "\n" +
                "h1. login scenario\n" +
                "\n" +
                "|| scenario || web ||\n" +
                "|Open browser at *http://www.google.com*|\n" +
                "|Type *test* in *GoogleSearchPage.search*|\n" +
                "\n";

        ITestPage testPage = par.readString(testString, null);
        Assert.assertNotNull(testPage);
        Assert.assertNotNull(testPage.getBlocks());
        
        int i = 0;
        IBlock block = testPage.getBlocks().get(i);
        Assert.assertEquals("comment", block.getBlockType());
        CommentBlock commentBlock = (CommentBlock) block;
        Assert.assertEquals(1, commentBlock.getLines().size());
        i++;
        
        Assert.assertNotNull(testPage.getBlocks().get(i));
        Assert.assertEquals("webPageBlock", testPage.getBlocks().get(i).getBlockType());
        WebPageBlock webPage = (WebPageBlock) testPage.getBlocks().get(i);
        List<WebPageConfigLine> blockLines = webPage.getBlockLines();
        Assert.assertEquals(1,blockLines.size());
        Assert.assertEquals("GoogleSearchPage", webPage.getFixtureName());
        WebPageConfigLine configLine = blockLines.get(0);
        Assert.assertEquals("search",configLine.getElementName());
        Assert.assertEquals("input",configLine.getType());
        Assert.assertEquals("lst-ib",configLine.getLocator());
        Assert.assertEquals("ID",configLine.getMethod());
        Assert.assertEquals(new Integer(0),configLine.getPosition());
        i++;
        
        Assert.assertEquals("comment", testPage.getBlocks().get(i).getBlockType());
        i++;
        
        Assert.assertEquals("test", testPage.getBlocks().get(i).getBlockType());
        i++;
    }

}
