package us.minevict.mvutil.common;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A version for MV-Util.
 * <p>
 * This is usually used to make sure a feature is part of the version on the server.
 * </p>
 *
 * @since 0.2.2
 */
public final class MvUtilVersion implements Comparable<MvUtilVersion> {
  private static final MvUtilVersion currentVersion = readManifestVersion();
  /**
   * The major version number.
   */
  private final int major;
  /**
   * The minor version number.
   */
  private final int minor;
  /**
   * The patch version number.
   */
  private final int patch;

  /**
   * Constructs a new version.
   *
   * @param major The major version number.
   * @param minor The minor version number.
   * @param patch The patch version number.
   */
  public MvUtilVersion(int major, int minor, int patch) {
    this.major = major;
    this.minor = minor;
    this.patch = patch;
  }

  /**
   * Constructs a new version.
   *
   * @param major The major version number.
   * @param minor The minor version number.
   */
  public MvUtilVersion(int major, int minor) {
    this(major, minor, 0);
  }

  /**
   * Constructs a new version.
   *
   * @param major The major version number.
   */
  public MvUtilVersion(int major) {
    this(major, 0);
  }

  private static MvUtilVersion readManifestVersion() {
    String[] version = MvUtilVersion.class.getPackage().getImplementationVersion()
        .trim()
        .split("[-_]", 2)[0]
        .split("\\.");
    String majorText = version[0].replaceAll("[^0-9]", "");
    String minorText = version[1].replaceAll("[^0-9]", "");
    String patchText = version[2].replaceAll("[^0-9]", "");
    int major = Integer.parseInt(majorText);
    int minor = Integer.parseInt(minorText);
    int patch = Integer.parseInt(patchText);
    return new MvUtilVersion(major, minor, patch);
  }

  /**
   * Gets the current version of MV-Util.
   *
   * @return The current version of MV-Util.
   */
  @NotNull
  public static MvUtilVersion getVersion() {
    return currentVersion;
  }

  /**
   * Require the version given to be on the server.
   *
   * @param version The version required.
   * @throws VersionException If the version present is less than the one required.
   */
  public static void requireVersion(@NotNull MvUtilVersion version) {
    if (getVersion().compareTo(version) < 0) {
      throw new VersionException(version);
    }
  }

  /**
   * Require the version given to be on the server.
   *
   * @param major The major version number required.
   * @param minor The minor version number required.
   * @param patch The patch version number required.
   * @throws VersionException If the version present is less than the one required.
   */
  public static void requireVersion(int major, int minor, int patch) {
    requireVersion(new MvUtilVersion(major, minor, patch));
  }

  /**
   * Require the version given to be on the server.
   *
   * @param major The major version number required.
   * @param minor The minor version number required.
   * @throws VersionException If the version present is less than the one required.
   */
  public static void requireVersion(int major, int minor) {
    requireVersion(new MvUtilVersion(major, minor));
  }

  /**
   * Require the version given to be on the server.
   *
   * @param major The major version number required.
   * @throws VersionException If the version present is less than the one required.
   */
  public static void requireVersion(int major) {
    requireVersion(new MvUtilVersion(major));
  }

  /**
   * Gets the major version number of this version.
   *
   * @return The current major version number.
   */
  public int getMajor() {
    return major;
  }

  /**
   * Gets the minor version number of this version.
   *
   * @return The current minor version number.
   */
  public int getMinor() {
    return minor;
  }

  /**
   * Gets the patch version number of this version.
   *
   * @return The current patch version number.
   */
  public int getPatch() {
    return patch;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MvUtilVersion)) {
      return false;
    }
    MvUtilVersion that = (MvUtilVersion) o;
    return getMajor() == that.getMajor() &&
        getMinor() == that.getMinor() &&
        getPatch() == that.getPatch();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMajor(), getMinor(), getPatch());
  }

  @Override
  public String toString() {
    return getMajor() + "." + getMinor() + "." + getPatch();
  }

  @Override
  public int compareTo(@NotNull MvUtilVersion other) {
    if (other == this) {
      return 0;
    }
    if (other.getMajor() != getMajor()) {
      return Integer.compare(getMajor(), other.getMajor());
    }
    if (other.getMinor() != getMinor()) {
      return Integer.compare(getMinor(), other.getMinor());
    }
    return Integer.compare(getPatch(), other.getPatch());
  }

  /**
   * An exception thrown if the version of MV-Util expected is higher than the one present.
   */
  public static final class VersionException extends RuntimeException {
    public VersionException(final MvUtilVersion minimum) {
      super("version of MV-Util is v" + MvUtilVersion.getVersion() + " whereas at least v" + minimum + " was expected");
    }
  }
}
