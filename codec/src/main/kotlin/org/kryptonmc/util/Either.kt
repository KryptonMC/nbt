/*
 * This file is part of Krypton NBT, licensed under the MIT license.
 *
 * Copyright (C) 2021 KryptonMC and contributors
 *
 * This project is licensed under the terms of the MIT license.
 * For more details, please reference the LICENSE file in the top-level directory.
 */
package org.kryptonmc.util

import org.kryptonmc.util.functional.App
import org.kryptonmc.util.functional.Applicative
import org.kryptonmc.util.functional.CocartesianLike
import org.kryptonmc.util.functional.K1
import org.kryptonmc.util.functional.Traversable
import java.util.Optional
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function

/**
 * A type that represents a left or a right type.
 */
public sealed interface Either<L, R> : App<Either.Mu<R>, L> {

    public val left: L?
        get() = null

    public val right: R?
        get() = null

    public fun left(): Optional<L> = Optional.ofNullable(left)

    public fun right(): Optional<R> = Optional.ofNullable(right)

    public fun ifLeft(consumer: Consumer<L>): Either<L, R> = this

    public fun ifRight(consumer: Consumer<R>): Either<L, R> = this

    public fun <T> map(leftMapper: Function<L, T>, rightMapper: Function<R, T>): T

    public fun <T> mapLeft(mapper: Function<L, T>): Either<T, R> = map({ left(mapper.apply(it)) }, { right(it) })

    public fun <T> mapRight(mapper: Function<R, T>): Either<L, T> = map({ left(it) }, { right(mapper.apply(it)) })

    public fun <A, B> mapBoth(leftMapper: Function<L, A>, rightMapper: Function<R, B>): Either<A, B>

    public fun <L2> flatMap(function: Function<L, Either<L2, R>>): Either<L2, R> = map(function) { right(it) }

    public fun orThrow(): L = map({ it }) {
        if (it is Throwable) throw RuntimeException(it)
        throw RuntimeException(it.toString())
    }

    public fun swap(): Either<R, L> = map({ right(it) }, { left(it) })

    public class Mu<R> : K1

    @JvmRecord
    private data class Left<L, R>(override val left: L) : Either<L, R> {

        override fun ifLeft(consumer: Consumer<L>): Either<L, R> = apply { consumer.accept(left) }

        override fun <T> map(leftMapper: Function<L, T>, rightMapper: Function<R, T>): T = leftMapper.apply(left)

        override fun <A, B> mapBoth(leftMapper: Function<L, A>, rightMapper: Function<R, B>): Either<A, B> = Left(leftMapper.apply(left))

        override fun equals(other: Any?): Boolean = this === other || (other is Left<*, *> && left == other.left)

        override fun hashCode(): Int = 31 + left.hashCode()

        override fun toString(): String = "Left($left)"
    }

    @JvmRecord
    private data class Right<L, R>(override val right: R) : Either<L, R> {

        override fun ifRight(consumer: Consumer<R>): Either<L, R> = apply { consumer.accept(right) }

        override fun <T> map(leftMapper: Function<L, T>, rightMapper: Function<R, T>): T = rightMapper.apply(right)

        override fun <A, B> mapBoth(leftMapper: Function<L, A>, rightMapper: Function<R, B>): Either<A, B> = Right(rightMapper.apply(right))

        override fun equals(other: Any?): Boolean = this === other || (other is Right<*, *> && right == other.right)

        override fun hashCode(): Int = 31 + right.hashCode()

        override fun toString(): String = "Right($right)"
    }

    public class Instance<R2> : Applicative<Mu<R2>, Instance.Mu<R2>>,
        Traversable<Mu<R2>, Instance.Mu<R2>>,
        CocartesianLike<Mu<R2>, R2, Instance.Mu<R2>> {

        override fun <T, R> map(function: Function<T, R>, ts: App<Either.Mu<R2>, T>): App<Either.Mu<R2>, R> = unbox(ts).mapLeft(function)

        override fun <A> point(a: A): App<Either.Mu<R2>, A> = left(a)

        override fun <A, R> lift1(function: App<Either.Mu<R2>, Function<A, R>>): Function<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, R>> =
            Function { a -> unbox(function).flatMap { f -> unbox(a).mapLeft(f) } }

        override fun <A, B, R> lift2(
            function: App<Either.Mu<R2>, BiFunction<A, B, R>>
        ): BiFunction<App<Either.Mu<R2>, A>, App<Either.Mu<R2>, B>, App<Either.Mu<R2>, R>> =
            BiFunction { a, b -> unbox(function).flatMap { f -> unbox(a).flatMap { av -> unbox(b).mapLeft { bv -> f.apply(av, bv) } } } }

        override fun <F : K1, A, B> traverse(
            applicative: Applicative<F, *>,
            function: Function<A, App<F, B>>,
            input: App<Either.Mu<R2>, A>
        ): App<F, App<Either.Mu<R2>, B>> = unbox(input)
            .map({ left -> applicative.ap({ left(it) }, function.apply(left)) }, { applicative.point(right(it)) })

        override fun <A> to(input: App<Either.Mu<R2>, A>): App<Either.Mu<R2>, A> = input

        override fun <A> from(input: App<Either.Mu<R2>, A>): App<Either.Mu<R2>, A> = input

        public class Mu<R2> : Applicative.Mu, Traversable.Mu, CocartesianLike.Mu
    }

    public companion object {

        @JvmStatic
        public fun <L, R> unbox(box: App<Mu<R>, L>): Either<L, R> = box as Either<L, R>

        @JvmStatic
        public fun <L, R> left(value: L): Either<L, R> = Left(value)

        @JvmStatic
        public fun <L, R> right(value: R): Either<L, R> = Right(value)
    }
}