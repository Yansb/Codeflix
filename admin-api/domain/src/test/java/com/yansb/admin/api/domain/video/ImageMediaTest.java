package com.yansb.admin.api.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageMediaTest {

  @Test
  public void givenValidParams_whenCallsNewImage_shouldReturnNewInstance() {
    final var expectedChecksum = "checksum";
    final var expectedName = "name.png";
    final var expectedLocation = "/image/location";

    final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

    Assertions.assertNotNull(actualImage);
    Assertions.assertEquals(expectedChecksum, actualImage.checksum());
    Assertions.assertEquals(expectedName, actualImage.name());
    Assertions.assertEquals(expectedLocation, actualImage.location());
  }

  @Test
  public void givenTwoEqualMedias_whenCallsEquals_shouldReturnTrue() {
    final var expectedChecksum = "checksum";
    final var expectedLocation = "/image/location";

    final var img1 = ImageMedia.with(expectedChecksum, "image1.png", expectedLocation);
    final var img2 = ImageMedia.with(expectedChecksum, "image2.png", expectedLocation);

    Assertions.assertEquals(img1, img2);
    Assertions.assertNotSame(img1, img2);
  }

  @Test
  public void givenInvalidParams_whenCallsWith_shouldReturnError() {
    Assertions.assertThrows(NullPointerException.class, () -> ImageMedia.with(null, "name", "location"));
    Assertions.assertThrows(NullPointerException.class, () -> ImageMedia.with("checksum", null, "location"));
    Assertions.assertThrows(NullPointerException.class, () -> ImageMedia.with("checksum", "name", null));
  }

}