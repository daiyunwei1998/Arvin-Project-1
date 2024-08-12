"use client"
import React, { useState } from 'react'
import { Button, Form, Input, Upload, Divider, Space, Select, InputNumber, message} from "antd";
import { UploadOutlined } from '@ant-design/icons';
import ProductAdminImageUpload from '@/app/components/ProductAdminImageUpload';
import ProductAdminSizeSelector from '@/app/components/ProductAdminSizeSelector';
import ProductAdminColorSelect from '@/app/components/ProductAdminColorSelect';
import NameColorInput from '@/app/components/NameColorInput';
const { Option } = Select;
const { TextArea } = Input;
export default function page() {
  const [nameColorPairs, setNameColorPairs] = useState([]); 
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();
  const [variants, setVariants] = useState([]);

  const handleAddVariant = () => {
    setVariants([...variants, { color: '', name: '', stock: '' }]);
  };

  const handleChange = (index, field, value) => {
    const newVariants = [...variants];
    newVariants[index][field] = value;
    setVariants(newVariants);
  };


  
  const handleAdd = (pair) => {
    setNameColorPairs([...nameColorPairs, pair]);
    const currentColors = form.getFieldValue('colors') || [];
  form.setFieldsValue({ colors: [...currentColors, pair] });
};
  const onFinish = async (values) => {
    setLoading(true);
    const formData = new FormData();

    // Append form values
    for (const [key, value] of Object.entries(values)) {
      if (key === 'colors' && Array.isArray(value)) {
        value.forEach((color) => formData.append('colors[]', color));
      } else if (Array.isArray(value)) {
        value.forEach((v) => formData.append(`${key}[]`, v));
      } else {
        formData.append(key, value);
      }
    }

    // Append variants as an array of objects
  if (variants && Array.isArray(variants)) {
    const variantsArray = variants.map((variant) => ({
      variant_color: variant.color,
      variant_size: variant.size,
      variant_stock: variant.stock
    }));
    formData.append('variants', JSON.stringify(variantsArray));
  }

    // Append main image
  const mainImageFile = values.main_image && values.main_image[0].originFileObj;
  if (mainImageFile) {
    formData.append('main_image', mainImageFile);
  }

 // Append images
if (values.images) {
  console.log('appending images')
  values.images.forEach(file => {
    formData.append('images', file.originFileObj);
  });
}

if (values.colors && Array.isArray(values.colors)) {
  formData.append('colors', JSON.stringify(values.colors));
}

    try {  ///api/v1/products/create   for rewrite
      const response = await fetch('https://stylish.yunweidai.net/api/v1/products/create', {
        method: 'POST',
        body: formData,
      });

      
      const result = await response.json();
      console.log(result);
      message.success("Product created successfully");
  
    } catch (error) {
      console.error('Error submitting form:', error);
    } finally {
      setLoading(false);
    }
  };

  const onTextAreaChange = (e) => {
    console.log('Change:', e.target.value);
  };
  const handleImageChange = (fileList) => {
    console.log('handleImageChange:', fileList);
    form.setFieldsValue({
      images: fileList,
    });

    
  };
  return (
    <Form 
    form={form}
    onFinish={onFinish}
    name="wrap"
    labelCol={{
      flex: '110px',
    }}
    labelAlign="left"
    labelWrap
    wrapperCol={{
      flex: 1,
    }}
    colon={false}
    style={{
      maxWidth: 600,
    }}
  >
     <Form.Item
      label="Product title"
      name="title"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Input />
    </Form.Item>
    <Form.Item
      label="price"
      name="price"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Input prefix="$" suffix="TWD"  />
    </Form.Item>

    <Form.Item
      label="category"
      name="category"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Select options={[
        { value: 'men', label: 'Men' },
        { value: 'women', label: 'Women' },
        { value: 'accessories', label: 'Accessories' }
      ]}>
   
  </Select>
    </Form.Item>

    <Form.Item
      label="description"
      name="description"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Input />
    </Form.Item>
    
    <Form.Item
      label="texture"
      name="texture"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Input />
    </Form.Item>

      
    <Form.Item
      label="wash"
      name="wash"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Input />
    </Form.Item>

      
    <Form.Item
      label="place"
      name="place"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Input />
    </Form.Item>

    <Form.Item
      label="note"
      name="note"
      rules={[
        {
          required: true,
        },
      ]}
    >
      <Input />
    </Form.Item>
    <Form.Item
      label="Story"
      name="story"
    >
    <TextArea showCount maxLength={999} onChange={onTextAreaChange} placeholder="" />
    </Form.Item>
   
    <Form.Item
      label="colors"
      name="colors"
      initialValue={[]}
    >
    <NameColorInput onAdd={handleAdd}></NameColorInput>
    </Form.Item>

    <Form.Item
      label="sizes"
      name="sizes"
      initialValue={['S', 'M', 'L']}
      rules={[
        {
          type: 'array'
        },
      ]}
    >
    <ProductAdminSizeSelector></ProductAdminSizeSelector>
    </Form.Item>


    <Form.Item
      label="main_image"
      name="main_image"
      valuePropName= "fileList"
    getValueFromEvent={(e) => {
      if (Array.isArray(e)) {
        return e;
      }
      return e && e.fileList;
    }}
    >
    <Upload
      beforeUpload={() => false}
      listType="picture"
      defaultFileList={[]}
      maxCount={1}
    >
      <Button icon={<UploadOutlined />}>Upload</Button>
    </Upload>
   </Form.Item>

   <Form.Item
      label="images"
      name="images"
      valuePropName="fileList"
    getValueFromEvent={(e) => {
      if (Array.isArray(e)) {
        return e;
      }
      return e && e.fileList;
    }}
    >
    <ProductAdminImageUpload onChange={handleImageChange}></ProductAdminImageUpload>
   </Form.Item>

   {variants.map((variant, index) => (
          <div key={index}>
            <Divider orientation="left">Variant {index + 1}</Divider>
            <Space direction="vertical" style={{ width: '100%' }}>
              <Form.Item label="Variant Color">
                <Input
                  value={variant.color}
                  onChange={(e) => handleChange(index, 'color', e.target.value)}
                />
              </Form.Item>
              <Form.Item label="Variant Size">
              <Select
                value={variant.size}
                onChange={(value) => handleChange(index, 'size', value)}
              >
                <Option value="S">S</Option>
                <Option value="M">M</Option>
                <Option value="L">L</Option>
              </Select>
              </Form.Item>
              <Form.Item label="Stock">
              <InputNumber value = {variant.stock} onChange={(value) => handleChange(index, 'stock', value)}
               min={0} max={999}/>
               
              </Form.Item>
            </Space>
            <Divider />
          </div>
        ))}
        <Form.Item>
          <Button type="primary" onClick={handleAddVariant}>
            Add Variant
          </Button>
        </Form.Item>

    <Form.Item label=" ">
      <Button type="primary" htmlType="submit">
        Submit
      </Button>
    </Form.Item>
   
  </Form>
  )
}
