/*
 * Galaxy
 * Copyright (C) 2012 Parallel Universe Software Co.
 * 
 * This file is part of Galaxy.
 *
 * Galaxy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * Galaxy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with Galaxy. If not, see <http://www.gnu.org/licenses/>.
 */
package co.paralleluniverse.common.util;

/**
 *
 * @author pron
 */
public final class Enums {
    public static <E extends Enum<E>> long setOf() {
        return 0L;
    }
    
    public static <E extends Enum<E>> long setOf(E e1) {
        return (1L << e1.ordinal());
    }

    public static <E extends Enum<E>> long setOf(E e1, E e2) {
        long set = 0;
        set |= (1L << e1.ordinal());
        set |= (1L << e2.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long setOf(E e1, E e2, E e3) {
        long set = 0;
        set |= (1L << e1.ordinal());
        set |= (1L << e2.ordinal());
        set |= (1L << e3.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long setOf(E e1, E e2, E e3, E e4) {
        long set = 0;
        set |= (1L << e1.ordinal());
        set |= (1L << e2.ordinal());
        set |= (1L << e3.ordinal());
        set |= (1L << e4.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long setOf(E e1, E e2, E e3, E e4, E e5) {
        long set = 0;
        set |= (1L << e1.ordinal());
        set |= (1L << e2.ordinal());
        set |= (1L << e3.ordinal());
        set |= (1L << e4.ordinal());
        set |= (1L << e5.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long setOf(E e1, E e2, E e3, E e4, E e5, E e6) {
        long set = 0;
        set |= (1L << e1.ordinal());
        set |= (1L << e2.ordinal());
        set |= (1L << e3.ordinal());
        set |= (1L << e4.ordinal());
        set |= (1L << e5.ordinal());
        set |= (1L << e6.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long setOf(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        long set = 0;
        set |= (1L << e1.ordinal());
        set |= (1L << e2.ordinal());
        set |= (1L << e3.ordinal());
        set |= (1L << e4.ordinal());
        set |= (1L << e5.ordinal());
        set |= (1L << e6.ordinal());
        set |= (1L << e7.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long setOf(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        long set = 0;
        set |= (1L << e1.ordinal());
        set |= (1L << e2.ordinal());
        set |= (1L << e3.ordinal());
        set |= (1L << e4.ordinal());
        set |= (1L << e5.ordinal());
        set |= (1L << e6.ordinal());
        set |= (1L << e7.ordinal());
        set |= (1L << e8.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long setOf(E... es) {
        long set = 0;
        for (E e : es)
            set |= (1L << e.ordinal());
        return set;
    }

    public static <E extends Enum<E>> long rangeBetween(E from, E to) {
        return (-1L >>>  (from.ordinal() - to.ordinal() - 1)) << from.ordinal();
    }
    
    public static boolean intersects(long set1, long set2) {
        return 0L != (set1 & set2);
    }
    
    public static <E extends Enum<E>> boolean isIn(E e, long set) {
        return intersects(setOf(e), set);
    }
    
    private Enums() {
    }
}
