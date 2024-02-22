package org.example.persistence.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

@Repository
public class DirectoryRepository {

  private final ResourceLoader resourceLoader;

  public DirectoryRepository(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  /**
   * resources 配下のファイルを指定した場所にコピーする
   * コピー先のディレクトリが存在しない場合は作成する
   *
   * @param resourceLocation resources 配下のファイルパス (例: "classpath:templates/README.md")
   * @param targetLocation   コピー先のファイルパス
   *
   * @return コピーしたファイルのサイズ
   *
   * @throws IOException ファイルのコピーに失敗した場合
   */
  public Long copyResource(String resourceLocation, String targetLocation) throws IOException {
    Path targetPath = Path.of(targetLocation);
    Files.createDirectories(targetPath.getParent());
    Resource sourceReadmeResource = resourceLoader.getResource(resourceLocation);
    return Files.copy(sourceReadmeResource.getInputStream(), targetPath,
        StandardCopyOption.REPLACE_EXISTING);
  }
}
