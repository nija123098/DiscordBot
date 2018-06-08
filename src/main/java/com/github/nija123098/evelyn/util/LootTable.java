package com.github.nija123098.evelyn.util;

import com.github.nija123098.evelyn.exception.DevelopmentException;
import javafx.util.Pair;

import java.util.*;

/**
 * A utility for loot tables including guaranteed,
 * shared probability, and mutually exclusive loot.
 *
 * @author nija123098
 * @param <E> the type of loot the table gets.
 */
public class LootTable<E> implements Cloneable {
    private List<E> guaranteed = new LinkedList<>();
    private Map<Float, LootTable<E>> shared = new HashMap<>();
    private float totalExclusiveFactor = 0;
    private ArrayList<Pair<Float, LootTable<E>>> exclusive = new ArrayList<>();

    /**
     * Adds items to be garenteed returns of the loot table.
     *
     * @param es the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addGuaranteed(E...es) {
        Collections.addAll(this.guaranteed, es);
        return this;
    }

    /**
     * Adds items to be guaranteed returns of the loot table.
     *
     * @param es the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addGuaranteed(List<E> es) {
        this.guaranteed.addAll(es);
        return this;
    }

    /**
     * Adds items to be guaranteed returns of the loot table
     *
     * @param table the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addGuaranteed(LootTable<E> table) {
        return addShared(0, table);
    }

    /**
     * Adds items to be shared returns of the loot table.
     * If the chosen probability is greater then or equal to the item,
     * the item will be returned at the winnings.
     *
     * @param es the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addShared(float prob, E...es) {
        this.shared.put(prob, (LootTable<E>) new LootTable<>().addGuaranteed(es));
        return this;
    }

    /**
     * Adds items to be shared returns of the loot table.
     * If the chosen probability is greater then or equal to the item,
     * the item will be returned at the winnings.
     *
     * @param es the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addShared(float prob, List<E> es) {
        this.shared.put(prob, (LootTable<E>) new LootTable<>().addGuaranteed(es));
        return this;
    }

    /**
     * Adds items to be shared returns of the loot table.
     * If the chosen probability is greater then or equal to the item,
     * the table's winnings will be returned at the winnings.
     *
     * @param table the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addShared(float prob, LootTable<E> table) {
        this.shared.put(prob, table);
        return this;
    }

    /**
     * Adds items to be mutually exclusive returns of the loot table.
     * If the chosen probability is within the range of the exclusive
     * it is chosen to be the exclusive pool reward.
     *
     * @param es the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addExclusive(float prob, E...es) {
        this.totalExclusiveFactor += prob;
        this.exclusive.add(new Pair<>(this.totalExclusiveFactor, (LootTable<E>) new LootTable<>().addGuaranteed(es)));
        return this;
    }

    /**
     * Adds items to be mutually exclusive returns of the loot table.
     * If the chosen probability is within the range of the exclusive
     * it is chosen to be the exclusive pool reward.
     *
     * @param es the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addExclusive(float prob, List<E> es) {
        this.totalExclusiveFactor += prob;
        this.exclusive.add(new Pair<>(this.totalExclusiveFactor, (LootTable<E>) new LootTable<>().addGuaranteed(es)));
        return this;
    }

    /**
     * Adds items to be mutually exclusive returns of the loot table.
     * If the chosen probability is within the range of the exclusive
     * it's winnings chosen to be the exclusive pool reward.
     *
     * @param table the items to add as guaranteed returns of the loot table.
     * @return the instance.
     */
    public LootTable<E> addExclusive(float prob, LootTable<E> table) {
        this.totalExclusiveFactor += prob;
        this.exclusive.add(new Pair<>(this.totalExclusiveFactor, (LootTable<E>) new LootTable<>().addGuaranteed(table)));
        return this;
    }

    /**
     * Gets the list of winnings from the loot table.
     *
     * @param random the random for supplying the random values.
     * @param take if the winnings should be taken from the instance so they can not be won again.
     * @return the list of winnings.
     */
    public List<E> getWinnings(Random random, boolean take) {
        List<E> winnings = new LinkedList<>(this.guaranteed);
        if (take) this.guaranteed.clear();
        float val = random.nextFloat();
        this.shared.forEach((prob, eLootTable) -> {
            if (prob <= val) winnings.addAll(eLootTable.getWinnings(random, take));
        });
        float weightedVal = this.totalExclusiveFactor * val;
        Iterator<Pair<Float, LootTable<E>>> iterator = this.exclusive.iterator();
        Pair<Float, LootTable<E>> pair;
        while (true) {
            if (!iterator.hasNext()) throw new DevelopmentException("LootTable built incorrectly and does not properly get an exclusive loot");
            pair = iterator.next();
            if (pair.getKey() < weightedVal) {
                winnings.addAll(pair.getValue().getWinnings(random, take));
                break;
            }
        }
        return winnings;
    }

    /**
     * Gets the list of winnings from the loot table.
     *
     * @param take if the winnings should be taken from the instance so they can not be won again.
     * @return the list of winnings.
     */
    public List<E> getWinnings(boolean take) {
        return this.getWinnings(new Random(), take);
    }

    /**
     * Gets the list of winnings from the loot table.
     *
     * @return the list of winnings.
     */
    public List<E> getWinnings() {
        return this.getWinnings(new Random(), false);
    }

    @Override
    public LootTable clone() {
        try {
            LootTable<E> o = (LootTable<E>) super.clone();
            o.guaranteed = new ArrayList<>(this.guaranteed);
            o.shared = new HashMap<>(this.shared);
            this.shared.forEach((prob, eLootTable) -> o.shared.put(prob, eLootTable.clone()));
            o.exclusive = new ArrayList<>(this.exclusive.size());
            this.exclusive.forEach((pair) -> o.exclusive.add(new Pair<>(pair.getKey(), pair.getValue().clone())));
            return o;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
