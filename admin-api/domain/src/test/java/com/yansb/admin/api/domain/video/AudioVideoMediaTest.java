package com.yansb.admin.api.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AudioVideoMediaTest {


  @Test
  public void givenValidParams_whenCallsNewVideo_shouldReturnNewInstance() {
    final var expectedChecksum = "checksum";
    final var expectedName = "name.png";
    final var expectedRawLocation = "rawLocation";
    final var expectedEncodedLocation = "encodedLocation";
    final var expectedStatus = MediaStatus.PENDING;


    final var actualVideo = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

    Assertions.assertNotNull(actualVideo);
    Assertions.assertEquals(expectedChecksum, actualVideo.checksum());
    Assertions.assertEquals(expectedName, actualVideo.name());
    Assertions.assertEquals(expectedEncodedLocation, actualVideo.encodedLocation());
    Assertions.assertEquals(expectedRawLocation, actualVideo.rawLocation());
    Assertions.assertEquals(expectedStatus, actualVideo.status());
  }

  @Test
  public void givenTwoEqualMedias_whenCallsEquals_shouldReturnTrue() {
    final var expectedChecksum = "checksum";
    final var expectedRawLocation = "/video/location";

    final var video1 = AudioVideoMedia.with(expectedChecksum, "random", expectedRawLocation, expectedRawLocation, MediaStatus.PENDING);
    final var video2 = AudioVideoMedia.with(expectedChecksum, "other", expectedRawLocation, expectedRawLocation, MediaStatus.COMPLETED);

    Assertions.assertEquals(video1, video2);
    Assertions.assertNotSame(video1, video2);
  }

  @Test
  public void givenInvalidParams_whenCallsWith_shouldReturnError() {
    Assertions.assertThrows(NullPointerException.class, () -> AudioVideoMedia.with(null, "name", "Rawlocation", "EncodedLocation", MediaStatus.PENDING));
    Assertions.assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", null, "Rawlocation", "EncodedLocation", MediaStatus.PENDING));
    Assertions.assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", "name", null, "EncodedLocation", MediaStatus.PENDING));
    Assertions.assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", "name", "Rawlocation", null, MediaStatus.PENDING));
    Assertions.assertThrows(NullPointerException.class, () -> AudioVideoMedia.with("checksum", "name", "Rawlocation", "EncodedLocation", null));
  }

}