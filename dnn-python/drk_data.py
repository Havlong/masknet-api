import os
import pandas as pd
os.environ["KERAS_BACKEND"] = "tensorflow"

import tensorflow as tf


def prep_embeddings():
    data_path = os.path.join('data', 'smoking_driking_dataset.csv')
    data = pd.read_csv(data_path)

    target_cols = ['SMK_stat_type_cd', 'DRK_YN']
    categorical_feats = ['sex', 'age', 'height', 'weight', 'hear_left', 'hear_right', 'urine_protein']
    numerical_feats = data.columns.drop(target_cols + categorical_feats).tolist()
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
        result_data[feat_name] = vocabs[vocab_idx][data[feat_name]]
    for feat_name in numerical_feats:
        result_data[feat_name] = data[feat_name]
    if 'DRK_YN' in data:
        result_data['target'] = 1 if data['DRK_YN'] == 'Y' else 0
    return result_data


def to_dataset(df, feats, target):
    return tf.data.Dataset.from_tensor_slices((dict(df[feats]), df[target]))


def prep_train_dataset(categorical_feats, numerical_feats, vocabs):
    data_path = os.path.join('data', 'smoking_driking_dataset.csv')
    data = pd.read_csv(data_path)

    data = preprocess_dataframe(data)
    return to_dataset(data, [*categorical_feats, *numerical_feats], 'DRK_YN')


