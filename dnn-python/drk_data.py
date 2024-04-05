import os
import pandas as pd

import tensorflow as tf


BATCH_SIZE = 256
BUFFER_SIZE = 1024 * 1024


def prep_embeddings():
    data_path = os.path.join('data', 'shorter_dataset.csv')
    data = pd.read_csv(data_path)

    target_cols = ['SMK_stat_type_cd', 'DRK_YN']
    categorical_feats = ['sex', 'age', 'height', 'weight', 'hear_left', 'hear_right', 'urine_protein']
    numerical_feats = data.columns.drop(target_cols + categorical_feats).tolist()
    categorical_feats = dict(data.dtypes[categorical_feats])
    vocabs = [{label: idx for idx, label in enumerate(data[feat].unique())} for feat in categorical_feats]

    return categorical_feats, numerical_feats, vocabs


def preprocess_dataframe(df, categorical_feats, numerical_feats, vocabs):
    result_df = df[[*categorical_feats, *numerical_feats]].copy()
    for vocab_idx, feat_name in enumerate(categorical_feats):
        result_df[feat_name] = result_df[feat_name].apply(vocabs[vocab_idx].get)
    result_df['target'] = df['DRK_YN'].apply(lambda x: x == 'Y').astype('int64')
    return result_df


def preprocess_data(data, categorical_feats, numerical_feats, vocabs):
    result_data = {}
    for vocab_idx, feat_name in enumerate(categorical_feats):
        result_data[feat_name] = vocabs[vocab_idx].get(data[feat_name], 0)
    for feat_name in numerical_feats:
        result_data[feat_name] = data[feat_name]
    if 'DRK_YN' in data:
        result_data['target'] = 1 if data['DRK_YN'] == 'Y' else 0
    return result_data


def to_dataset(df, feats, target):
    return tf.data.Dataset.from_tensor_slices((dict(df[feats]), df[target]))


def prep_train_dataset(categorical_feats, numerical_feats, vocabs):
    data_path = os.path.join('data', 'shorter_dataset.csv')
    data = pd.read_csv(data_path)

    data = preprocess_dataframe(data)
    train_ds = to_dataset(data, [*categorical_feats, *numerical_feats], 'DRK_YN')

    train_ds = train_ds.shuffle(BUFFER_SIZE, reshuffle_each_iteration=True)
    return train_ds.batch(BATCH_SIZE).prefetch(tf.data.AUTOTUNE)
