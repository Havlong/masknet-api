from keras import layers, ops, Sequential
from keras.saving import register_keras_serializable


@register_keras_serializable()
class MaskBlock(layers.Layer):
    def __init__(self, output_size, reduction_factor=2.0, **kwargs):
        super().__init__(**kwargs)
        self.reduction_factor = reduction_factor
        self.output_size = output_size

        self.layer_norm = layers.LayerNormalization()
        self.feed_forward = layers.Dense(self.output_size, use_bias=False)


    def build(self, input_shape):
        if len(input_shape) != 2:
            raise ValueError(f"A `MaskBlock` layer should be called on a list of exactly 2 inputs. Received: input_shape={input_shape}")

        input_size, mask_size = input_shape
        input_size = input_size[-1]
        mask_size = mask_size[-1]

        aggregation_units = int(input_size * self.reduction_factor)

        self.mask_layer = Sequential([
            layers.Input(shape=(mask_size,)),
            layers.Dense(aggregation_units, activation='relu'),
            layers.Dense(input_size)
        ])


    def call(self, inputs):
        if not isinstance(inputs, (list, tuple)) or len(inputs) != 2:
            raise ValueError(f"A `MaskBlock` layer should be called on a list of exactly 2 inputs. Received: inputs={inputs} (not a list of tensors)")
        
        net_inputs, emb_inputs = inputs
        
        output_mask = self.mask_layer(emb_inputs)
        
        output = self.feed_forward(output_mask * net_inputs)
        output = self.layer_norm(output)
        output = ops.relu(output)

        return output


    def get_config(self):
        config = super().get_config()
        config.update({
            "output_size": self.output_size,
            "reduction_factor": self.reduction_factor
        })
        
        return config
