/**
 * MV-Util
 * Copyright (C) 2020 Mariell Hoversholm, Nahuel Dolores
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package us.minevict.mvutil.common

/**
 * A version of MV-Util.
 *
 * This is usually used to make sure a feature is part of the version on the server.
 *
 * @since 5.0.0
 */
data class MvUtilVersion(
    val major: Int,
    val minor: Int = 0,
    val patch: Int = 0
) : Comparable<MvUtilVersion> {
    override fun compareTo(other: MvUtilVersion): Int {
        if (other == this) return 0
        if (other.major != major) return major.compareTo(other.major)
        if (other.minor != minor) return minor.compareTo(other.minor)
        return patch.compareTo(other.patch)
    }

    override fun toString(): String {
        return "$major.$minor.$patch"
    }

    companion object {
        /**
         * Gets the current version of MV-Util.
         *
         * @return The current version of MV-Util.
         */
        val CURRENT_VERSION: MvUtilVersion by lazy {
            val version = MvUtilVersion::class.java.`package`.implementationVersion.trim()
                .split('_', '-', limit = 2).first()
                .split('.')
            MvUtilVersion(
                version.component1().filter(Char::isDigit).toInt(),
                version.component2().filter(Char::isDigit).toInt(),
                version.component3().filter(Char::isDigit).toInt()
            )
        }

        /**
         * Require the version given to be on the server.
         *
         * @param version The version required.
         * @throws VersionException If the version present is less than the one required.
         */
        @Throws(VersionException::class)
        @JvmStatic // Backwards compatibility to v0.2.2
        fun requireVersion(version: MvUtilVersion) {
            if (CURRENT_VERSION < version) {
                throw VersionException(version)
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
        @Throws(VersionException::class)
        @JvmStatic // Backwards compatibility to v0.2.2
        fun requireVersion(major: Int, minor: Int = 0, patch: Int = 0) {
            requireVersion(MvUtilVersion(major, minor, patch))
        }
    }

    /**
     * An exception thrown if the version of MV-Util expected is higher than the one present.
     */
    class VersionException(minimum: MvUtilVersion) : RuntimeException(
        "version of MV-Util is v$CURRENT_VERSION whereas at least v$minimum was expected"
    )
}