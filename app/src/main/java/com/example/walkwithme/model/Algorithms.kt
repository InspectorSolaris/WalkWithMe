package com.example.walkwithme.model

import com.example.walkwithme.model.genetic.Genetic
import com.example.walkwithme.model.genetic.Genotype
import com.example.walkwithme.model.utilities.Random
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Algorithms {
    fun runGenetic(
        graph: Array<Array<Double>>,
        length: Double
    ): List<Int> {
        val genetic = Genetic<Int, Double>()

        genetic.setFitness { genotype ->
            if (genotype.fitness != null) {
                return@setFitness genotype
            }

            var len = 0.0
            (0 until genotype.genes.size - 1).forEach {
                val a = genotype.genes[it + 0]
                val b = genotype.genes[it + 1]

                len += graph[a][b]
            }

            val w = graph.size.toDouble()
            val l = 1.0 + abs(len - length) / length
            val fitness = w / (l * l * l * l * l)

            Genotype(
                genotype.genes,
                fitness
            )
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
            val genesPool = arrayListOf<Int>()

            (genotype.genes.first()..genotype.genes.last()).forEach { i ->
                genesPool.add(i)
            }

            genotype.genes.forEach { gene ->
                genes.add(gene)
                genesPool.remove(gene)
            }

            val pAdd = 0.3
            val pRemove = 0.3
            val pSwap = 0.8
            val iMax = genotype.genes.size * 2

            var add = Random.randDouble() < pAdd && genesPool.isNotEmpty()
            var remove = Random.randDouble() < pRemove && genes.size > 2
            var swap = Random.randDouble() < pSwap && genes.size > 3
            var i = 0

            while ((add || remove || swap) && i < iMax) {
                if (add) {
                    val genesInd = Random.randInt(1, genes.size)
                    val poolInd = Random.randInt(0, genesPool.size)

                    genes.add(genesInd, genesPool[poolInd])
                    genesPool.removeAt(poolInd)
                }
                if (remove) {
                    val genesInd = Random.randInt(1, genes.size - 1)

                    genesPool.add(genes[genesInd])
                    genes.removeAt(genesInd)
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

        return genetic.run(128, 512) {
            val genes = arrayListOf<Int>()
            val genesPool = arrayListOf<Int>()

            (1 until graph.size - 1).forEach { i ->
                genesPool.add(i)
            }

            val probability = 1.0 - 2.0 / (genesPool.size + 2)
            while (genesPool.isNotEmpty() && Random.randDouble() < probability) {
                val point = genesPool.random()

                genesPool.remove(point)
                genes.add(point)
            }

            genes.add(0, 0)
            genes.add(genes.size, graph.size - 1)

            Genotype(genes)
        }.genes
    }
}