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
     * TODO: �������������� DAO ���� ����� ����������� � ��������. ������� ����� ��������� ��� ���� ������ ��� �
     * TODO:� �����. ������ �������� ���������� � ������� ������ ������ ���� �������������.
     * TODO: 1)���������� ����� ��������� ��� ������ � �������.
     * TODO: 2)��������� ��� ������ � �������
     * TODO: 3)��������� ������������ ������ � ����� ������.
     */
    @Test
    public void testDAO() throws FileNotFoundException {
        String filePath = "";
        DictionaryReader dictionaryReaderFile = new DictionaryReader(filePath);
        dictionaryReaderFile.openConnection();
    }
}
