package com.example.walkwithme.model.genetic

import com.example.walkwithme.model.utilities.Random
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Example {
    private val len = 6
    private val points = listOf(
        listOf(+0.0, 0.0, 1.0),
        listOf(-1.0, 1.0, 1.0),
        listOf(-1.0, 2.0, 1.0),
        listOf(-1.0, 3.0, 1.0),
        listOf(+0.0, 2.0, 1.0),
        listOf(-1.0, 1.5, 1.0),
        listOf(-1.0, 2.5, 1.0),
        listOf(+0.0, 4.0, 1.0)
    )

    private fun distance(a: List<Double>, b: List<Double>): Double {
        return sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]))
    }

    fun run() {
        val genetic = Genetic<Int, Double>()

        genetic.setFitness { genotype ->
            if (genotype.fitness != null) {
                return@setFitness genotype
            }

            var l = 0.0
            (0 until genotype.genes.size - 1).forEach {
                val a = genotype.genes[it + 0]
                val b = genotype.genes[it + 1]

                l += distance(points[a], points[b])
            }

            val w = points.sumByDouble { it.last() }
            val dl = abs(l - len)
            val fitness = w / (dl * dl)
            Genotype(genotype.genes, fitness)
        }

        genetic.setBestOf { genotypeA, genotypeB ->
            if (genotypeA.fitness!! >= genotypeB.fitness!!) genotypeA else genotypeB
        }

        genetic.setCrossover { genotypeA, genotypeB ->
            val fa = genotypeA.fitness!!
            val fb = genotypeB.fitness!!
            val wa = fa / (fa + fb)
            val wb = fb / (fa + fb)

            val weightedGenes = hashMapOf<Int, Double>()
            genotypeA.genes.forEachIndexed { i, gene -> weightedGenes += mapOf(gene to wa * i) }
            genotypeB.genes.forEachIndexed { i, gene -> weightedGenes += mapOf(gene to wb * i) }

            val minBound = min(genotypeA.genes.size, genotypeB.genes.size)
            val maxBound = max(genotypeA.genes.size, genotypeB.genes.size)
            val size = Random.randInt(minBound, maxBound + 1)
            val genes = arrayListOf<Int>()

            (0..size).forEach { _ ->
                val gene = weightedGenes.minBy { it.value }!!

                genes.add(gene.key)
                weightedGenes.remove(gene.key)
            }

            if (!genes.contains(genotypeA.genes.first())) { genes.add(0, genotypeA.genes.first()) }
            if (!genes.contains(genotypeA.genes.last())) { genes.add(genes.size, genotypeA.genes.last()) }

            Genotype(genes)
        }

        genetic.setMutate { genotype ->
            val genes = arrayListOf<Int>()
            val genesPool = ArrayList(genotype.genes)

            genotype.genes.forEach { gene ->
                genes.add(gene)
                genesPool.remove(gene)
            }

            val pAdd = 0.25
            val pRemove = 0.25
            val pSwap = 0.25
            val iMax = genotype.genes.size

            var add = Random.randDouble() < pAdd && genesPool.isNotEmpty()
            var remove = Random.randDouble() < pRemove && genes.size > 2
            var swap = Random.randDouble() < pSwap && genes.size > 3
            var i = 0

            while ((add || remove || swap) && i < iMax) {
                if (add) {
                    val genesInd = Random.randInt(1, genes.size)
                    val poolInd = Random.randInt(0, genesPool.size - 1)

                    genes.add(genesInd, genesPool[poolInd])
                    genesPool.removeAt(poolInd)
                }
                if (remove) {
                    val genesInd = Random.randInt(1, genes.size - 1)

                    genesPool.add(genes[genesInd])
                    genes.remove(genesInd)
                }
                if (swap) {
                    val li = Random.randInt(1, genes.size - 1)
                    val ri = Random.randInt(1, genes.size - 1)

                    if (li != ri) {
                        val lv = genes[li]
                        val rv = genes[ri]

                        genes[ri] = lv
                        genes[li] = rv
                    }
                }

                add = Random.randDouble() < pAdd && genesPool.isNotEmpty()
                remove = Random.randDouble() < pRemove && genes.size > 2
                swap = Random.randDouble() < pSwap && genes.size > 3
                ++i
            }

            Genotype(genes)
        }

        genetic.setSelectToCrossover { genotypes, generationSize ->
            val selected = arrayListOf<Pair<Genotype<Int, Double>, Genotype<Int, Double>>>()
            val genotypesPool = ArrayList(genotypes)

            while (genotypes.size < generationSize / 2) {
                val genotypeA = genotypesPool.random()
                genotypesPool.remove(genotypeA)

                val genotypeB = genotypesPool.random()
                genotypesPool.remove(genotypeB)

                selected.add(Pair(genotypeA, genotypeB))
            }

            selected
        }

        genetic.setSelectToMutation { genotypes, generationSize ->
            val selected = arrayListOf<Genotype<Int, Double>>()
            val genotypesPool = ArrayList(genotypes)

            while (selected.size < generationSize / 4) {
                val genotype = genotypesPool.random()

                selected.add(genotype)
                genotypesPool.remove(genotype)
            }

            selected
        }

        genetic.setSelectToSelection { genotypes, generationSize ->
            val selected = arrayListOf<Genotype<Int, Double>>()
            val genotypesPool = ArrayList(genotypes)

            while (selected.size < generationSize) {
                val genotype = Random.randBy(genotypesPool) { it.fitness!! }

                selected.add(genotype)
                genotypesPool.remove(genotype)
            }

            selected
        }

        genetic.run(512, 2048) {
            val genes = arrayListOf<Int>()
            val genesPool = arrayListOf<Int>()
            val probability = 1 - 2 / (points.size - 2)

            (1 until points.size - 1).forEach {
                genesPool.add(it)
            }

            while (genesPool.isNotEmpty() && Random.randDouble() < probability) {
                val point = genesPool.random()

                genesPool.remove(point)
                genes.add(point)
            }

            genes.add(0, 0)
            genes.add(genes.size, points.size - 1)

            Genotype(genes)
        }
    }
}