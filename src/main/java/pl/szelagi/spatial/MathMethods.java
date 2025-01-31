/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.spatial;

public class MathMethods {
    public static boolean isBetween(double p, double a, double b) {
        double min = Math.min(a, b);
        double max = Math.max(a, b);
        return p >= min && p <= max;
    }
}
