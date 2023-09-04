import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class changeFileModifiedDate {
    public static void main(String[] args) {
        String dirPath = "."; // 当前目录
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 时间格式
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath), "*.md");
            for(Path path : stream){
                // 读取实际修改时间
                List<String> lines = Files.readAllLines(path);
                String date = lines.get(2).replace("date: ",""); // 日期所在行数 可自行调整
                LocalDateTime newDate = LocalDateTime.parse(date,formatter);
                // 设置修改时间
                BasicFileAttributeView attributes = Files.getFileAttributeView(path, BasicFileAttributeView.class);
                FileTime newTime = FileTime.fromMillis(convertLocalDateTimeToDateTime(newDate).getTime());
                attributes.setTimes(newTime, null, null);
                System.out.println(path.getFileName() + "文件的修改日期已经修改为：" + newDate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Date convertLocalDateTimeToDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
