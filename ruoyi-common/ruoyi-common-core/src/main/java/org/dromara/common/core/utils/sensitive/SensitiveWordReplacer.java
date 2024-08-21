package org.dromara.common.core.utils.sensitive;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.SensitiveProcessor;
import cn.hutool.dfa.WordTree;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词替换器
 *
 * @author hexm
 */
public class SensitiveWordReplacer {
    private final WordTree sensitiveTree = new WordTree();

    public SensitiveWordReplacer() {
    }

    public SensitiveWordReplacer(Collection<String> sensitiveWords) {
        sensitiveTree.addWords(sensitiveWords);
    }

    public void addWord(String word) {
        sensitiveTree.addWord(word);
    }

    public void addWords(Collection<String> sensitiveWords) {
        sensitiveTree.addWords(sensitiveWords);
    }

    public void clear() {
        sensitiveTree.clear();
    }

    /**
     * 判断是否包含敏感词
     *
     * @param text 文本
     * @return
     */
    public boolean containsSensitiveWord(String text) {
        return sensitiveTree.isMatch(text);
    }

    /**
     * 查找敏感词，返回找到的第一个敏感词
     *
     * @param text 文本
     * @return 敏感词
     */
    public FoundWord getFoundFirstSensitive(String text) {
        return sensitiveTree.matchWord(text);
    }

    /**
     * 查找敏感词，返回找到的所有敏感词
     *
     * @param text 文本
     * @return 敏感词
     */
    public List<FoundWord> getFoundAllSensitive(String text) {
        return sensitiveTree.matchAllWords(text);
    }

    /**
     * 查找敏感词，返回找到的所有敏感词<br>
     * 密集匹配原则：假如关键词有 ab,b，文本是abab，将匹配 [ab,b,ab]<br>
     * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     *
     * @param text           文本
     * @param isDensityMatch 是否使用密集匹配原则
     * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
     * @return 敏感词
     */
    public List<FoundWord> getFoundAllSensitive(String text, boolean isDensityMatch, boolean isGreedMatch) {
        return sensitiveTree.matchAllWords(text, -1, isDensityMatch, isGreedMatch);
    }

    /**
     * 处理过滤文本中的敏感词，默认替换成*
     *
     * @param text 文本
     * @return 敏感词过滤处理后的文本
     */
    public String sensitiveWordReplace(String text) {
        return sensitiveWordReplace(text, true, null);
    }

    /**
     * 处理过滤文本中的敏感词，默认替换成*
     *
     * @param text               文本
     * @param isGreedMatch       贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
     * @param sensitiveProcessor 敏感词处理器，默认按匹配内容的字符数替换成*
     * @return 敏感词过滤处理后的文本
     */
    public String sensitiveWordReplace(String text, boolean isGreedMatch, SensitiveProcessor sensitiveProcessor) {
        if (StrUtil.isEmpty(text)) {
            return text;
        }

        //敏感词过滤场景下，不需要密集匹配
        final List<FoundWord> foundWordList = getFoundAllSensitive(text, true, isGreedMatch);
        if (CollUtil.isEmpty(foundWordList)) {
            return text;
        }
        sensitiveProcessor = sensitiveProcessor == null ? new SensitiveProcessor() {
        } : sensitiveProcessor;

        final Map<Integer, FoundWord> foundWordMap = new HashMap<>(foundWordList.size(), 1);
        foundWordList.forEach(foundWord -> foundWordMap.put(foundWord.getStartIndex(), foundWord));
        final int length = text.length();
        final StringBuilder textStringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            final FoundWord fw = foundWordMap.get(i);
            if (fw != null) {
                textStringBuilder.append(sensitiveProcessor.process(fw));
                i = fw.getEndIndex();
            } else {
                textStringBuilder.append(text.charAt(i));
            }
        }
        return textStringBuilder.toString();
    }
}
