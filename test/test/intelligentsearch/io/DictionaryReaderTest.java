package test.intelligentsearch.io;

import com.myuniver.intelligentsearch.util.io.DictionaryReader;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * User: Dima
 * Date: 20.02.13
 * Time: 0:34
 */
public class DictionaryReaderTest {
    /**
     * TODO: Протестировать DAO слой между приложением и словарем. Словарь может храниться как базе данных так и
     * TODO:в файле. Способ открытия соединения и выборки данных должен быть универсальным.
     * TODO: 1)Обрисовать общий интерфейс для работы с данными.
     * TODO: 2)Проверить для работы с файлами
     * TODO: 3)Проверить правильность работы с базой данных.
     */
    @Test
    public void testDAO() throws FileNotFoundException {
        String filePath = "./resources/dictionary/dic_hist.txt";
        DictionaryReader dictionaryReaderFile = new DictionaryReader(filePath);
        dictionaryReaderFile.openConnection();
    }
}
