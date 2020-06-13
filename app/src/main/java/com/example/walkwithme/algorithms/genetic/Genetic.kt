package com.example.walkwithme.algorithms.genetic


class Genetic<T, V>(
) {
    private var _fitness:           ((Genotype<T, V>) -> Genotype<T, V>)? = null
    private var _crossover:         ((Genotype<T, V>, Genotype<T, V>) -> Genotype<T, V>)? = null
    private var _mutate:            ((Genotype<T, V>) -> Genotype<T, V>)? = null
    private var _selectToCrossover: ((List<Genotype<T, V>>) -> List<Pair<Genotype<T, V>, Genotype<T, V>>>)? = null
    private var _selectToMutation:  ((List<Genotype<T, V>>) -> List<Genotype<T, V>>)? = null
    private var _selectToSelection: ((List<Genotype<T, V>>) -> List<Genotype<T, V>>)? = null

    fun setFitness(fitness: (Genotype<T, V>) -> Genotype<T, V>) {
        _fitness = fitness
    }

    fun setCrossover(crossover: (Genotype<T, V>, Genotype<T, V>) -> Genotype<T, V>) {
        _crossover = crossover
    }

    fun setMutate(mutate: (Genotype<T, V>) -> Genotype<T, V>) {
        _mutate = mutate
    }

    fun setSelectToCrossover(selectToCrossover: (List<Genotype<T, V>>) -> List<Pair<Genotype<T, V>, Genotype<T, V>>>) {
        _selectToCrossover = selectToCrossover
    }

    fun setSelectToMutation(selectToMutation: (List<Genotype<T, V>>) -> List<Genotype<T, V>>) {
        _selectToMutation = selectToMutation
    }

    fun setSelectToSelection(selectToSelection: (List<Genotype<T, V>>) -> List<Genotype<T, V>>) {
        _selectToSelection = selectToSelection
    }

    fun run(
        generationSize: Int,
        generationNumber: Int,
        generator: () -> Genotype<T, V>
    ): List<List<T>>? {
        if (_fitness == null ||
            _crossover == null ||
            _mutate == null ||
            _selectToCrossover == null ||
            _selectToMutation == null ||
            _selectToSelection == null
        ) {
            return null
        }

        val generation = Generation<T, V>(
            { genotypeA, genotypeB  -> _fitness!!(_crossover!!(genotypeA, genotypeB)) },
            { genotype              -> _fitness!!(_mutate!!(genotype)) },
            { genotypes             -> _selectToCrossover!!(genotypes) },
            { genotypes             -> _selectToMutation!!(genotypes) },
            { genotypes             -> _selectToSelection!!(genotypes) }
        )

        generation.generate(generationSize) {
            _fitness!!(generator())
        }

        (0..generationNumber).forEach { _ ->
            generation.update()
        }

        return generation.genotypes.map { it.genes }
    }
}