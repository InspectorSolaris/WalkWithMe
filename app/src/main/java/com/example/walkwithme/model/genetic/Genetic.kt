package com.example.walkwithme.model.genetic


class Genetic<T, V> {
    private var _fitness: ((Genotype<T, V>) -> Genotype<T, V>)? = null
    private var _bestOf: ((Genotype<T, V>, Genotype<T, V>) -> Genotype<T, V>)? = null
    private var _crossover: ((Genotype<T, V>, Genotype<T, V>) -> Genotype<T, V>)? = null
    private var _mutate: ((Genotype<T, V>) -> Genotype<T, V>)? = null
    private var _selectToCrossover: ((List<Genotype<T, V>>, Int) -> List<Pair<Genotype<T, V>, Genotype<T, V>>>)? = null
    private var _selectToMutation: ((List<Genotype<T, V>>, Int) -> List<Genotype<T, V>>)? = null
    private var _selectToSelection: ((List<Genotype<T, V>>, Int) -> List<Genotype<T, V>>)? = null

    fun setFitness(fitness: (Genotype<T, V>) -> Genotype<T, V>) {
        _fitness = fitness
    }

    fun setCrossover(crossover: (Genotype<T, V>, Genotype<T, V>) -> Genotype<T, V>) {
        _crossover = crossover
    }

    fun setBestOf(bestOf: (Genotype<T, V>, Genotype<T, V>) -> Genotype<T, V>) {
        _bestOf = bestOf
    }

    fun setMutate(mutate: (Genotype<T, V>) -> Genotype<T, V>) {
        _mutate = mutate
    }

    fun setSelectToCrossover(selectToCrossover: (List<Genotype<T, V>>, Int) -> List<Pair<Genotype<T, V>, Genotype<T, V>>>) {
        _selectToCrossover = selectToCrossover
    }

    fun setSelectToMutation(selectToMutation: (List<Genotype<T, V>>, Int) -> List<Genotype<T, V>>) {
        _selectToMutation = selectToMutation
    }

    fun setSelectToSelection(selectToSelection: (List<Genotype<T, V>>, Int) -> List<Genotype<T, V>>) {
        _selectToSelection = selectToSelection
    }

    fun run(
        generationSize: Int,
        generationNumber: Int,
        generator: () -> Genotype<T, V>
    ): Genotype<T, V> {
        if (_fitness == null ||
            _bestOf == null ||
            _crossover == null ||
            _mutate == null ||
            _selectToCrossover == null ||
            _selectToMutation == null ||
            _selectToSelection == null
        ) {
            return Genotype(arrayListOf())
        }

        val generation = Generation<T, V>(
            { genotype -> _fitness!!(genotype) },
            { genotypeA, genotypeB -> _bestOf!!(genotypeA, genotypeB) },
            { genotypeA, genotypeB -> _crossover!!(genotypeA, genotypeB) },
            { genotype -> _mutate!!(genotype) },
            { genotypes -> _selectToCrossover!!(genotypes, generationSize) },
            { genotypes -> _selectToMutation!!(genotypes, generationSize) },
            { genotypes -> _selectToSelection!!(genotypes, generationSize) }
        )

        generation.generate(generationSize) { generator() }

        (0..generationNumber).forEach { _ ->
            generation.update()
        }

        return generation.best
    }
}